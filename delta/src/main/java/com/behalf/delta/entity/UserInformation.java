package com.behalf.delta.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInformation {
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
}