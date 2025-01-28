package com.behalf.delta.web;


import com.behalf.delta.entity.UserInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Backend: UserController.java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserInformation> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        UserInformation userInfo = new UserInformation(
                oidcUser.getEmail(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getPicture()
        );
        return ResponseEntity.ok(userInfo);
    }
}