package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutePagingRequest extends PagingRequest {
    private String departureKeyword;
    private String destinationKeyword;
    private List<String> departureCodes;
    private List<String> destinationCodes;
}
