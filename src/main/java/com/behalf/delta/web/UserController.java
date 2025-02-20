package com.behalf.delta.web;


import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.repo.UserInformationRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserInformationRepo userInformationRepo;

    @GetMapping("/me")
    @Cacheable(value = "userInfo", key = "#oidcUser != null ? #oidcUser.email : 'unknown'",  unless = "#result == null")
    public UserInformation getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        Optional<UserInformation> userInformationOptional = userInformationRepo.findByEmail(oidcUser.getEmail());

        UserInformation userInfo;

        if (userInformationOptional.isEmpty()){
            userInfo = UserInformation.builder().email(oidcUser.getEmail())
                    .firstName(oidcUser.getGivenName()).lastName(oidcUser.getFamilyName()).picture(oidcUser.getPicture()).build();
            userInformationRepo.save(userInfo);
        } else {
            userInfo = userInformationOptional.get();
        }

        return userInfo;
    }

    @GetMapping("/info")
    public Map<Long, UserInformation> getUserInfoById() {
        return userInformationRepo.findAll()
                .stream()
                .collect(Collectors.toMap(UserInformation::getId, user -> user));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request, HttpServletResponse response) {
        if (oidcUser != null) {
            new SecurityContextLogoutHandler().logout(request, response, null);

            // ✅ Redirect user to Google's logout page (optional: redirect back to login page)
            String logoutUrl = "https://accounts.google.com/Logout?continue=http://localhost:3000/login";

            return ResponseEntity.ok(logoutUrl); // ✅ Send logout URL to frontend
        }
        return ResponseEntity.ok("Success");
    }

}