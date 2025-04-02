package com.behalf.delta.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    String getCurrentUserEmail();
    boolean isUserAuthenticated();
    Collection<? extends GrantedAuthority> getUserAuthorities();
}