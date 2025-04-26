package com.fasfood.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransferType {
    @JsonProperty("in")
    IN,
    @JsonProperty("out")
    OUT
}
