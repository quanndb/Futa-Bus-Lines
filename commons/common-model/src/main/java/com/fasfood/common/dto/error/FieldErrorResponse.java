package com.fasfood.common.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class FieldErrorResponse implements Serializable {
    private String field;
    private String objectName;
    private String message;
}
