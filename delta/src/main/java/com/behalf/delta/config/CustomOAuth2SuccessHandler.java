package com.behalf.delta.config;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Value("setting.frontend-url")
    private String frontendRedirectUrl;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomOAuth2SuccessHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Get the OAuth2 authorized client
        OAuth2AuthorizedClient authorizedClient =
                authorizedClientService.loadAuthorizedClient(
                        "google", // Replace with your provider ID (e.g., google)
                        authentication.getName());

        // Extract the access token
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // Optionally extract user information (OIDC claims)
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        // Add token as a cookie
        Cookie tokenCookie = new Cookie("authToken", accessToken);
//        tokenCookie.setHttpOnly(true);
//        tokenCookie.setSecure(true); // Use true in production (HTTPS)
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(tokenCookie);

        response.sendRedirect("http://localhost:3000/post");
    }

}
