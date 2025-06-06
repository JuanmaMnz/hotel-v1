package com.hotel.Security.Account.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Permission {

    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_DELETE("employee:delete"),

    GUEST_CREATE("guest:create"),
    GUEST_READ("guest:read"),
    GUEST_UPDATE("guest:update"),
    GUEST_DELETE("guest:delete");

    private final String permission;
}