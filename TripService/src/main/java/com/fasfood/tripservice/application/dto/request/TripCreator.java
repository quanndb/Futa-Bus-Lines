package com.fasfood.tripservice.application.dto.request;

import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TripCreator {
    private String code;
    private String name;
    private String address;
    private String hotline;
    private TransitType transitType;
    private LocalTime arrivalTime;
    private TripTransitType tripTransitType;
}
