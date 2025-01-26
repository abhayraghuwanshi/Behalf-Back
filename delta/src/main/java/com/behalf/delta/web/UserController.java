package com.behalf.delta.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @GetMapping("/api/user/profile")
    public Map<String, Object> getUserProfile(OAuth2AuthenticationToken authentication) {
        return Map.of(
                "name", authentication.getPrincipal().getAttribute("name"),
                "email", authentication.getPrincipal().getAttribute("email")
        );
    }

}
