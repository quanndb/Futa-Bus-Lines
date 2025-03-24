package com.fasfood.storageservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFileCmd {
    private String name;
    private String type;
    private Boolean sharing;
    private Long size;
}
