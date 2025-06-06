package com.hotel.Security.Account.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    EMPLOYEE(
            Set.of(
                    Permission.EMPLOYEE_CREATE,
                    Permission.EMPLOYEE_READ,
                    Permission.EMPLOYEE_UPDATE,
                    Permission.EMPLOYEE_DELETE
            )
    ),

    GUEST(
            Set.of(
                    Permission.GUEST_CREATE,
                    Permission.GUEST_READ,
                    Permission.GUEST_UPDATE,
                    Permission.GUEST_DELETE
            )
    );

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    private final Set<Permission> permissions;

}