package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.TransitPointCreateOrUpdateCmd;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class TransitPoint extends Domain {
    private String name;
    private String address;
    private String hotline;
    private TransitType type;

    public TransitPoint(TransitPointCreateOrUpdateCmd cmd) {
        super();
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.hotline = cmd.getHotline();
        this.type = cmd.getType();
    }

    public TransitPoint update(TransitPointCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.hotline = cmd.getHotline();
        this.type = cmd.getType();
        return this;
    }
}
