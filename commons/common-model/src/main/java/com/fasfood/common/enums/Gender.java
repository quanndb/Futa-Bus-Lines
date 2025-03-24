package com.fasfood.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("Nữ"),
    MALE("Nam"),
    OTHER("Khác");

    private final String value;
}
