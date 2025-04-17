package com.behalf.delta.auth;

import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.repo.UserInformationRepo;
import com.behalf.delta.service.QuestSessionService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component("securityService")
public class SecurityRules {

    private final UserInformationRepo userInformationRepo;

    public SecurityRules(UserInformationRepo userInformationRepo) {
        this.userInformationRepo = userInformationRepo;
    }

    // Checks if user is part of a session

    public boolean isSameUser(String email, Long userId) {
        Optional<UserInformation> user  = userInformationRepo.findByEmail(email);
        return user.filter(userInformation -> Objects.equals(userInformation.getId(), userId)).isPresent();
    }
}