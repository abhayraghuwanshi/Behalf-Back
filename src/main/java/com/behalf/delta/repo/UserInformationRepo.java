package com.behalf.delta.repo;

import com.behalf.delta.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepo extends JpaRepository<UserInformation, Long> {

    Optional<UserInformation> findByEmail(String email);
}
