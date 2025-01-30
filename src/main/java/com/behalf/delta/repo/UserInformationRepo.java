package com.behalf.delta.repo;

import com.behalf.delta.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInformationRepo extends JpaRepository<UserInformation, Long> {

    Optional<UserInformation> findByEmail(String email);

    @Query("SELECT ui FROM UserInformation ui WHERE ui.id IN :ids")
    List<UserInformation> findAllById(@Param("ids") List<Long> ids);

}
