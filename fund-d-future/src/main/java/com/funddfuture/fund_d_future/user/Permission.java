package com.funddfuture.fund_d_future.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    FUNDER_READ("funder:read"),
    FUNDER_UPDATE("funder:update"),
    FUNDER_CREATE("funder:create"),
    FUNDER_DELETE("funder:delete")
    ;

    private final String permission;
}
