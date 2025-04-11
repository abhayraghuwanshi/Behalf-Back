package com.behalf.store.repo;

import com.behalf.store.model.UserRole;
import com.behalf.delta.entity.UserInformation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser(UserInformation user);

    List<UserRole> findByRole(String role);

    boolean existsByUserAndRole(UserInformation user, String role);

    void deleteByUserAndRole(UserInformation user, String role);

    @Cacheable("managers")
    List<UserRole> findByRoleIgnoreCase(String role);
}
