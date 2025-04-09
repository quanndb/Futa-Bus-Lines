package com.fasfood.tripservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceCreateOrUpdateCmd {
    private String name;
    private String address;
    private String code;
}
