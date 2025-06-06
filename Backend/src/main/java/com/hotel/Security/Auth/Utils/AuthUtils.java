package com.hotel.Security.Auth.Utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Security.Auth.DTO.AuthResponse;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthUtils {

    public static AuthResponse createAuthResponse(String accessToken, Set<String> roles) {
        return new AuthResponse(accessToken, roles);
    }

}