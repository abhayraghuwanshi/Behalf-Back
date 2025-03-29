package com.behalf.delta.auth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@Slf4j
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found");
            throw new UnauthorizedException("No authenticated user");
        }

        // Handle different types of authentication principals
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            // For OIDC (OpenID Connect) authentication
            return oidcUser.getEmail();
        } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            // For standard Spring Security UserDetails
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof String principalString) {
            // Fallback for string-based principal
            return principalString;
        }

        log.warn("Unable to extract email from principal: {}",
                authentication.getPrincipal().getClass().getName());
        throw new UnauthorizedException("Cannot retrieve user email");
    }

    @Override
    public boolean isUserAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                && authentication.getPrincipal().equals("anonymousUser"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getUserAuthorities() {
        Authentication authentication = getAuthentication();
        return authentication != null
                ? authentication.getAuthorities()
                : Collections.emptyList();
    }
}