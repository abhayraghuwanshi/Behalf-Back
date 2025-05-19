package com.behalf.store.model;

import com.behalf.delta.entity.UserInformation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role; // ADMIN, MANAGER, USER, etc.

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInformation user;
}
