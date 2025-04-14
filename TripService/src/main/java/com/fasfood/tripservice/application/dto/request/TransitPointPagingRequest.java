package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransitPointPagingRequest extends PagingRequest {
    private List<TransitType> types;
}
