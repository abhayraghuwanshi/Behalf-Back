package com.behalf.Eparser.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
                Message fullMessage = service.users().messages().get(user, message.getId()).execute();
                log.info("✉️ Subject: {}", extractSubject(fullMessage));
                // Optionally: processMessage(fullMessage);
            }

        } catch (IOException | GeneralSecurityException e) {
            log.error("❌ Error fetching airline emails", e);
            throw new RuntimeException("Failed to fetch airline emails", e);
        }
    }

    private Gmail createGmailService(String accessToken) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        return new Gmail.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private String extractSubject(Message message) {
        if (message.getPayload() == null || message.getPayload().getHeaders() == null) {
            return "(No Payload)";
        }

        return message.getPayload().getHeaders().stream()
                .filter(header -> "Subject".equalsIgnoreCase(header.getName()))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("(No Subject)");
    }
}
