package com.fasfood.tripservice.domain.cmd;

import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitPointCreateOrUpdateCmd {
    private String name;
    private String address;
    private String hotline;
    private TransitType type;
}
