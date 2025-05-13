package com.fasfood.tripservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripCreateOrUpdateCmd {
    private String code;
    private String name;
    private String description;
    private List<TripTransitCreateOrUpdateCmd> transits;
}
