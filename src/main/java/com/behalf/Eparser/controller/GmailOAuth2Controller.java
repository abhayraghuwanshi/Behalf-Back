package com.behalf.Eparser.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import com.behalf.Eparser.service.GmailOAuth2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/gmail/oauth2")
@RequiredArgsConstructor
public class GmailOAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(GmailOAuth2Controller.class);

    private final GmailOAuth2Service gmailOAuth2Service;

    /**
     * Redirect user to Google's OAuth 2.0 authorization screen.
     */
    @GetMapping("/authorize")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = gmailOAuth2Service.buildAuthorizationUrl();
        logger.info("Redirecting to Google OAuth URL: {}", url);
        response.sendRedirect(url);
    }

    /**
     * Handle the OAuth 2.0 callback from Google and exchange the code for tokens.
     */
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam(required = false) String code) {
        if (code == null || code.isBlank()) {
            logger.warn("Authorization code missing in callback.");
            return ResponseEntity.badRequest().body("Missing authorization code");
        }

        try {
            gmailOAuth2Service.exchangeCodeForTokens(code);
            logger.info("Gmail access successfully granted.");
            return ResponseEntity.ok("Gmail access granted.");
        } catch (Exception e) {
            logger.error("Error while exchanging code for tokens", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to authorize Gmail access.");
        }
    }
}

