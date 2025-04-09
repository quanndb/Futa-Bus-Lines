package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlacePagingRequest extends PagingRequest {
    private List<String> codes;
}
