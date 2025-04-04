package com.behalf.delta.service;

import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.entity.UserRole;
import com.behalf.delta.repo.UserInformationRepo;
import com.behalf.delta.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserInformationRepo userInformationRepo;

    @Transactional
    @CacheEvict(value = {"managers", "admins"}, allEntries = true)
    public void assignRole(Long userId, String role) {
        UserInformation user = userInformationRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean exists = userRoleRepository.existsByUserAndRole(user, role.toUpperCase());
        if (!exists) {
            UserRole userRole = UserRole.builder().user(user).role(role.toUpperCase()).build();
            userRoleRepository.save(userRole);
        }
    }

    @Transactional
    @CacheEvict(value = {"managers", "admins"}, allEntries = true)
    public void removeRole(Long userId, String role) {
        UserInformation user = userInformationRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRoleRepository.deleteByUserAndRole(user, role.toUpperCase());
    }

    @Cacheable(
            value = "roleUsersCache",
            key = "#role.toUpperCase()",
            condition = "#role.toUpperCase() == 'MANAGER' || #role.toUpperCase() == 'ADMIN'"
    )
    public List<UserRole> getUsersByRole(String role) {
        return userRoleRepository.findByRole(role.toUpperCase());
    }

    public boolean isAdminOrManager(Long userId) {
        UserInformation user = userInformationRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userRoleRepository.existsByUserAndRole(user, "ADMIN") ||
                userRoleRepository.existsByUserAndRole(user, "MANAGER");
    }


}

