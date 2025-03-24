package com.fasfood.iamservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RolePagingRequest extends PagingRequest {
    private List<Boolean> isRoots;
}
