package com.behalf.Eparser.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailOAuth2Service {

    @Value("${gmail.oauth2.client-id}")
    private String clientId;

    @Value("${gmail.oauth2.client-secret}")
    private String clientSecret;

    @Value("${gmail.oauth2.redirect-uri}")
    private String redirectUri;

    @Value("${gmail.oauth2.scopes}")
    private String scopes;

    private final GmailFetchService gmailFetchService;

    private final RestTemplate restTemplate = new RestTemplate();

    public String buildAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scopes)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .toUriString();
    }

    public void exchangeCodeForTokens(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri); // Must exactly match redirect used in auth URL
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Important for parsing JSON

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    "https://oauth2.googleapis.com/token",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            log.debug("GoogleTokenResponse: {}", rawResponse);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(rawResponse.getBody());

            String accessToken = jsonNode.path("access_token").asText(null);
            String refreshToken = jsonNode.path("refresh_token").asText(null);
            int expiresIn = jsonNode.path("expires_in").asInt(0);

            if (accessToken != null) {
                log.info("✅ Access Token: {}", accessToken);
                log.info("🔁 Refresh Token: {}", refreshToken);
                log.info("⏳ Expires In: {} seconds", expiresIn);

                // ✅ Use token to fetch emails
                gmailFetchService.fetchAirlineEmails(accessToken);
            } else {
                throw new RuntimeException("Access token not present in Google response");
            }

        } catch (Exception e) {
            log.error("Exception while exchanging code for tokens", e);
            throw new RuntimeException("Failed to get access token from Google", e);
        }
    }



}
