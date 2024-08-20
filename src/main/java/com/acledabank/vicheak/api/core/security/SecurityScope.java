package com.acledabank.vicheak.api.core.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SecurityScope {

    ADMIN("SCOPE_ADMIN"),
    STAFF("SCOPE_STAFF");

    private final String scope;

}
