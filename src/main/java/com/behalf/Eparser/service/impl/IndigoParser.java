package com.behalf.Eparser.service.impl;

import com.behalf.Eparser.model.FlightDetails;
import com.behalf.Eparser.service.AirlineParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IndigoParser implements AirlineParser {

    // Common Indian airport codes
    private static final String[] INDIAN_AIRPORTS = {
            "DEL", "BOM", "BLR", "HYD", "MAA", "CCU", "AMD", "GOI",
            "PNQ", "PAT", "JAI", "LKO", "VGA", "COK", "TRV"
    };

    @Override
    public FlightDetails parse(String emailBody) {
        try {
            // Try HTML parsing first (IndiGo emails are usually HTML-based)
            Document doc = Jsoup.parse(emailBody);

            // Pattern 1: Modern booking confirmation format
            Elements flightDivs = doc.select("div.flight-info, div.journey-details");
            if (!flightDivs.isEmpty()) {
                return parseModernFormat(flightDivs.first());
            }

            // Pattern 2: Older text-based format
            return parseLegacyTextFormat(emailBody);

        } catch (Exception e) {
            return parseUsingRegexFallback(emailBody);
        }
    }

    private FlightDetails parseModernFormat(Element flightDiv) {
        Elements routeElements = flightDiv.select("div.route, span.flight-route");
        Elements dateElements = flightDiv.select("div.date, span.travel-date");

        if (routeElements.isEmpty() || dateElements.isEmpty()) {
            return null;
        }

        String routeText = routeElements.first().text();
        String[] airports = extractAirports(routeText);

        if (airports.length != 2) {
            return null;
        }

        LocalDate date = parseIndigoDate(dateElements.first().text());
        return new FlightDetails(airports[0], airports[1], date);
    }

    private FlightDetails parseLegacyTextFormat(String text) {
        // Pattern for: "DEL → BLR | 15 Dec 2023"
        Pattern legacyPattern = Pattern.compile(
                "([A-Z]{3})\\s*[→➔-]+\\s*([A-Z]{3})\\s*\\|\\s*(\\d{1,2}\\s+[A-Za-z]{3}\\s+\\d{4})"
        );

        Matcher m = legacyPattern.matcher(text);
        if (m.find()) {
            return new FlightDetails(
                    m.group(1),
                    m.group(2),
                    parseIndigoDate(m.group(3))
            );
        }
        return null;
    }

    private FlightDetails parseUsingRegexFallback(String text) {
        // Fallback pattern for raw text
        Pattern fallbackPattern = Pattern.compile(
                "(?i)(?:from|departure).*?(" + String.join("|", INDIAN_AIRPORTS) + ")" +
                        ".*?(?:to|arrival).*?(" + String.join("|", INDIAN_AIRPORTS) + ")" +
                        ".*?(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]* \\d{1,2},? \\d{4})"
        );

        Matcher m = fallbackPattern.matcher(text);
        if (m.find()) {
            return new FlightDetails(
                    m.group(1),
                    m.group(2),
                    parseIndigoDate(m.group(3))
            );
        }
        return null;
    }

    private String[] extractAirports(String routeText) {
        // Handle formats like: "DEL - BLR" or "Delhi (DEL) → Bangalore (BLR)"
        Pattern airportPattern = Pattern.compile("([A-Z]{3})");
        Matcher m = airportPattern.matcher(routeText);

        String[] airports = new String[2];
        int i = 0;
        while (m.find() && i < 2) {
            airports[i++] = m.group(1);
        }
        return airports;
    }

    private LocalDate parseIndigoDate(String dateStr) {
        try {
            // Handle multiple date formats
            DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("dd MMM yyyy"),
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("MMM dd, yyyy")
            };

            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateStr.replaceAll("[^a-zA-Z0-9]", " ").trim(), formatter);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            log.warn("Failed to parse IndiGo date: {}", dateStr);
        }
        return null;
    }
}