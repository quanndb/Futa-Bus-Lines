package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.PlaceCreateOrUpdateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Place extends Domain {
    private String name;
    private String address;
    private String code;

    public Place(PlaceCreateOrUpdateCmd cmd) {
        super();
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.code = cmd.getCode();
    }

    public Place update(PlaceCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.code = cmd.getCode();
        return this;
    }
}
