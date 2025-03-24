package com.fasfood.iamservice.application.mapper;

import com.fasfood.iamservice.application.dto.request.AccountPagingRequest;
import com.fasfood.iamservice.application.dto.request.PermissionPagingRequest;
import com.fasfood.iamservice.application.dto.request.RolePagingRequest;
import com.fasfood.iamservice.domain.query.AccountPagingQuery;
import com.fasfood.iamservice.domain.query.PermissionPagingQuery;
import com.fasfood.iamservice.domain.query.RolePagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IamQueryMapper {
    AccountPagingQuery from(AccountPagingRequest request);

    RolePagingQuery from(RolePagingRequest request);

    PermissionPagingQuery from(PermissionPagingRequest request);
}
