package com.acledabank.vicheak.api.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SortUtil {

    DIRECTION("_direction"),
    FIELD("_field");

    private final String label;

}
