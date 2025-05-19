package com.behalf.Eparser.service;

import com.behalf.Eparser.model.FlightDetails;
import com.behalf.Eparser.model.ParserFactory;
import com.behalf.Eparser.service.impl.GenericParser;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;
@Slf4j
@Service
public class GmailFetchService {

    private static final String APPLICATION_NAME = "Behalf Gmail Airline Reader";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public void fetchAirlineEmails(String accessToken) {
        try {
            Gmail service = createGmailService(accessToken);
            String user = "me";
            String query = "subject:ticket OR subject:flight OR subject:booking";

            ListMessagesResponse response = service.users().messages().list(user).setQ(query).execute();
            List<Message> messages = response.getMessages();


            if (messages == null || messages.isEmpty()) {
                log.info("📭 No airline-related emails found.");
                return;
            }

            for (Message message : messages) {
                Message fullMessage = service.users().messages()
                        .get(user, message.getId())
                        .setFormat("FULL")
                        .execute();
                log.info(fullMessage.toString());
                processEmail(fullMessage);
            }

        } catch (IOException | GeneralSecurityException e) {
            log.error("❌ Error fetching airline emails", e);
            throw new RuntimeException("Failed to fetch airline emails", e);
        }
    }

    private void processEmail(Message email) {
        try {
            String airline = detectAirline(email);
            AirlineParser parser = ParserFactory.getParser(airline);
            String emailBody = extractEmailBody(email);
            log.info("Email body {}", emailBody);
            log.info("Airline parser {}", parser);
            log.info("airline {}", airline);

            FlightDetails details = parser.parse(emailBody);

            if (details.isValid()) {
                log.info("✅ Successfully parsed {} flight: {}", airline, details);
            } else {
                log.warn("⚠️ Partial parse for {} email: {}", airline, details);
                new GenericParser().parse(emailBody);
            }
        } catch (Exception e) {
            log.error("🔴 Failed to process email: {}", e.getMessage());
        }
    }

    private String detectAirline(Message email) {
        // Method 1: Check sender address
        String from = extractHeader(email, "From");
        if (from.contains("@united.com")) return "UNITED";
        if (from.contains("@delta.com")) return "DELTA";
        if (from.contains("@americanairlines.com")) return "AMERICAN";
        if (from.contains("@goindigo.in")) return "INDIGO";

        // Method 2: Check subject/body content
        String subject = extractSubject(email);
        String body = extractEmailBody(email);
        if (subject.contains("British Airways")) return "BRITISH_AIRWAYS";
        if (body.contains("Emirates Booking")) return "EMIRATES";

        // Method 3: Check HTML elements
        try {
            Document doc = Jsoup.parse(body);
            if (doc.select("img[src*=southwest]").size() > 0) return "SOUTHWEST";
            if (doc.select("div.lufthansa-booking").size() > 0) return "LUFTHANSA";
        } catch (Exception e) {
            log.warn("HTML parsing error during airline detection");
        }

        return "UNKNOWN";
    }

    private String extractEmailBody(Message message) {
        try {
            StringBuilder body = new StringBuilder();
            message.getPayload().getParts().forEach(part -> {
                if (part.getMimeType().equals("text/plain")) {
                    byte[] data = Base64.getMimeDecoder().decode(part.getBody().getData());
                    body.append(new String(data));
                }
            });
            return body.length() > 0 ? body.toString() : decodeBody(message.getPayload());
        } catch (Exception e) {
            log.error("Error extracting email body", e);
            return "";
        }
    }

    private String decodeBody(MessagePart part) {
        try {
            return new String(Base64.getMimeDecoder().decode(part.getBody().getData()));
        } catch (Exception e) {
            return "";
        }
    }

    private String extractSubject(Message message) {
        return extractHeader(message, "Subject");
    }

    private String extractHeader(Message message, String headerName) {
        return message.getPayload().getHeaders().stream()
                .filter(header -> header.getName().equalsIgnoreCase(headerName))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }

    private Gmail createGmailService(String accessToken) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
            request.getHeaders().setContentType("application/json");
        };

        return new Gmail.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}