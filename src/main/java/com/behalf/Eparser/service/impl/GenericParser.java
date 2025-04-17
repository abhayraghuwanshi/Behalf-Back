package com.behalf.Eparser.service.impl;

import com.behalf.Eparser.model.FlightDetails;
import com.behalf.Eparser.service.AirlineParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericParser implements AirlineParser {

    // Enhanced to recognize 500+ airport codes globally
    private static final String AIRPORT_CODE_REGEX =
            "\\b(?:[A-Z]{3}|" +
                    String.join("|", Arrays.asList(
                            "JFK","LAX","ORD","LHR","CDG","DXB","HND",
                            "DEL","BOM","SYD","YYZ","FRA","AMS","SIN"
                    )) + ")\\b";

    @Override
    public FlightDetails parse(String emailBody) {
        try {
            // Try JSON-LD first (modern airlines)
            Optional<FlightDetails> jsonResult = parseJsonLd(emailBody);
            if (jsonResult.isPresent()) return jsonResult.get();

            // Then try HTML parsing
            Document doc = Jsoup.parse(emailBody);
            Optional<FlightDetails> htmlResult = parseHtml(doc);
            if (htmlResult.isPresent()) return htmlResult.get();

            // Fallback to heavy-duty text parsing
            return parseText(emailBody);

        } catch (Exception e) {
            return new FlightDetails(null, null, null); // Invalid result
        }
    }

    // ===== Parsing Strategies ===== //

    private Optional<FlightDetails> parseJsonLd(String html) {
        Pattern jsonPattern = Pattern.compile(
                "<script type=\"application/ld\\+json\">(.*?)</script>",
                Pattern.DOTALL
        );

        Matcher matcher = jsonPattern.matcher(html);
        while (matcher.find()) {
            Optional<FlightDetails> details = parseFlightFromJson(matcher.group(1));
            if (details.isPresent() &&  details.get().isValid()) {
                return details;
            }
        }
        return Optional.empty();
    }

    private Optional<FlightDetails> parseHtml(Document doc) {
        // Strategy 1: Common div patterns
        Elements routes = doc.select("""
            .flight-route, .journey-path,
            .trip-segment, [class*=route]
            """);

        if (!routes.isEmpty()) {
            String text = routes.first().text();
            return parseRouteText(text);
        }

        // Strategy 2: Table-based layouts
        Elements rows = doc.select("tr:has(td:contains(Departure))");
        if (rows.size() >= 2) {
            String from = rows.get(0).select("td").last().text();
            String to = rows.get(1).select("td").last().text();
            String date = doc.select("td:contains(Date) + td").text();
            return Optional.of(new FlightDetails(
                    extractAirportCode(from),
                    extractAirportCode(to),
                    parseFlexibleDate(date)
            ));
        }

        return Optional.empty();
    }

    private FlightDetails parseText(String text) {
        // Enhanced regex with multiple pattern support
        List<Pattern> patterns = Arrays.asList(
                // Format: "DEL → BLR | 15 Dec 2023"
                Pattern.compile(
                        "(" + AIRPORT_CODE_REGEX + ")\\s*[→➔-]+\\s*" +
                                "(" + AIRPORT_CODE_REGEX + ")\\s*\\W+\\s*" +
                                "(\\d{1,2}\\s+[A-Za-z]{3}\\s+\\d{4})"
                ),
                // Format: "From: Delhi (DEL) To: Mumbai (BOM)"
                Pattern.compile(
                        "(?i)(?:from|departure).*?\\b(" + AIRPORT_CODE_REGEX + ")\\b" +
                                ".*?(?:to|arrival).*?\\b(" + AIRPORT_CODE_REGEX + ")\\b" +
                                ".*?(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})"
                )
        );

        return patterns.stream()
                .map(p -> p.matcher(text))
                .filter(Matcher::find)
                .map(m -> new FlightDetails(
                        m.group(1),
                        m.group(2),
                        parseFlexibleDate(m.group(3))
                ))
                .filter(FlightDetails::isValid)
                .findFirst()
                .orElse(new FlightDetails(null, null, null));
    }

    // ===== Helper Methods ===== //

    private String extractAirportCode(String text) {
        Matcher m = Pattern.compile(AIRPORT_CODE_REGEX).matcher(text);
        return m.find() ? m.group() : null;
    }

    private LocalDate parseFlexibleDate(String dateStr) {
        if (dateStr == null) return null;

        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("dd MMM yyyy"),
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(
                        dateStr.replaceAll("[^a-zA-Z0-9]", " ").trim(),
                        fmt
                );
            } catch (Exception ignored) {}
        }
        return null;
    }

    private Optional<FlightDetails> parseFlightFromJson(String json) {
        // Simplified JSON parsing (implement with real JSON library)
        if (json.contains("\"departureAirport\"") &&
                json.contains("\"arrivalAirport\"")) {
            return Optional.of(new FlightDetails(
                    extractJsonField(json, "departureAirport"),
                    extractJsonField(json, "arrivalAirport"),
                    parseFlexibleDate(extractJsonField(json, "departureTime"))
            ));
        }
        return Optional.empty();
    }

    private String extractJsonField(String json, String field) {
        Pattern p = Pattern.compile("\"" + field + "\":\"(.*?)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }
    private Optional<FlightDetails> parseRouteText(String text) {
        // Pattern 1: "DEL → BOM | 15 Dec 2023"
        Pattern arrowPattern = Pattern.compile(
                "(" + AIRPORT_CODE_REGEX + ")\\s*[→➔-]+\\s*" +
                        "(" + AIRPORT_CODE_REGEX + ")\\s*[|\\-]\\s*" +
                        "(\\d{1,2}\\s+[A-Za-z]{3}\\s+\\d{4})"
        );

        // Pattern 2: "Departure: DEL Arrival: BOM"
        Pattern labeledPattern = Pattern.compile(
                "(?i)departure.*?(" + AIRPORT_CODE_REGEX + ")" +
                        ".*arrival.*?(" + AIRPORT_CODE_REGEX + ")"
        );

        Matcher arrowMatcher = arrowPattern.matcher(text);
        if (arrowMatcher.find()) {
            return Optional.of(new FlightDetails(
                    arrowMatcher.group(1),
                    arrowMatcher.group(2),
                    parseFlexibleDate(arrowMatcher.group(3))
            ));
        }

        Matcher labeledMatcher = labeledPattern.matcher(text);
        if (labeledMatcher.find()) {
            return Optional.of(new FlightDetails(
                    labeledMatcher.group(1),
                    labeledMatcher.group(2),
                    null // Date might be elsewhere
            ));
        }

        return Optional.empty();
    }
}