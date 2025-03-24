package com.fasfood.iamservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Role;
import com.fasfood.iamservice.domain.RolePermission;
import com.fasfood.iamservice.domain.repository.RoleRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.RolePermissionEntity;
import com.fasfood.iamservice.infrastructure.persistence.mapper.RolePermissionEntityMapper;
import com.fasfood.iamservice.infrastructure.persistence.repository.RolePermissionEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RoleRepositoryImpl extends AbstractDomainRepository<Role, RoleEntity, UUID>
        implements RoleRepository {

    private final RolePermissionEntityRepository rolePermissionEntityRepository;
    private final RolePermissionEntityMapper rolePermissionEntityMapper;

    protected RoleRepositoryImpl(JpaRepository<RoleEntity, UUID> jpaRepository,
                                 EntityMapper<Role, RoleEntity> mapper,
                                 RolePermissionEntityRepository rolePermissionEntityRepository,
                                 RolePermissionEntityMapper rolePermissionEntityMapper) {
        super(jpaRepository, mapper);
        this.rolePermissionEntityRepository = rolePermissionEntityRepository;
        this.rolePermissionEntityMapper = rolePermissionEntityMapper;
    }

    @Override
    public List<Role> saveAll(List<Role> domains) {
        Objects.requireNonNull(domains);
        List<RolePermission> rolePermissions = new ArrayList<>();
        domains.forEach(domain -> {
            rolePermissions.addAll(domain.getPermissions());
        });
        this.rolePermissionEntityRepository.saveAll(this.rolePermissionEntityMapper.toEntity(rolePermissions));
        return super.saveAll(domains);
    }

    @Override
    protected List<Role> enrichList(List<Role> roles) {
        Objects.requireNonNull(roles);

        List<RolePermissionEntity> rolePermissionEntities = this.rolePermissionEntityRepository
                .findAllByRoleIds((roles.stream()
                        .map(Role::getId).collect(Collectors.toList())));

        Map<UUID, List<RolePermission>> rolePermissionMap = this.rolePermissionEntityMapper.toDomain(rolePermissionEntities)
                .stream().collect(Collectors.groupingBy(RolePermission::getRoleId));

        roles.forEach(role -> {
            if (rolePermissionMap.containsKey(role.getId())) {
                role.enrichPermissions(new HashSet<>(rolePermissionMap.get(role.getId())));
            }
            else {
                role.enrichPermissions(new HashSet<>());
            }
        });
        return roles;
    }

    @Override
    public Role getById(UUID id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.ROLE_NOTFOUND));
    }
}
