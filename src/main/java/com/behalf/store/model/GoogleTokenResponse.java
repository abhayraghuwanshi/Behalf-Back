package com.behalf.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTokenResponse {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn; // ✔ Integer instead of Long
    private String scope;
    private String tokenType;
    private String idToken;


}
