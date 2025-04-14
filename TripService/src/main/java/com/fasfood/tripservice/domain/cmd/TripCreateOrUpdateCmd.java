package com.fasfood.tripservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripCreateOrUpdateCmd {
    private String code;
    private List<TripTransitCreateOrUpdateCmd> transits;
}
