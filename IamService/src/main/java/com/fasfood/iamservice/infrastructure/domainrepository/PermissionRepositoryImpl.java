package com.fasfood.iamservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Permission;
import com.fasfood.iamservice.domain.repository.PermissionRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.iamservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PermissionRepositoryImpl extends AbstractDomainRepository<Permission, PermissionEntity, UUID>
        implements PermissionRepository {

    protected PermissionRepositoryImpl(JpaRepository<PermissionEntity, UUID> jpaRepository,
                                       EntityMapper<Permission, PermissionEntity> mapper) {
        super(jpaRepository, mapper);
    }

    @Override
    public Permission getById(UUID id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.PERMISSION_NOTFOUND));
    }
}
