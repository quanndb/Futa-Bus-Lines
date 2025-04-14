package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutePagingRequest extends PagingRequest {
    private String departureKeyword;
    private String destinationKeyword;
}
