package com.fasfood.storageservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.storageservice.domain.File;
import com.fasfood.storageservice.domain.FileAction;
import com.fasfood.storageservice.domain.repository.FileRepository;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import com.fasfood.storageservice.infrastructure.persistence.mapper.FileActionEntityMapper;
import com.fasfood.storageservice.infrastructure.persistence.repository.FileActionEntityRepository;
import com.fasfood.storageservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileRepositoryImpl extends AbstractDomainRepository<File, FileEntity, UUID> implements FileRepository {

    private final FileActionEntityRepository fileActionEntityRepository;
    private final FileActionEntityMapper fileActionEntityMapper;

    protected FileRepositoryImpl(JpaRepository<FileEntity, UUID> jpaRepository,
                                 EntityMapper<File, FileEntity> mapper,
                                 FileActionEntityRepository fileActionEntityRepository,
                                 FileActionEntityMapper fileActionEntityMapper) {
        super(jpaRepository, mapper);
        this.fileActionEntityRepository = fileActionEntityRepository;
        this.fileActionEntityMapper = fileActionEntityMapper;
    }

    @Override
    public List<File> saveAll(List<File> domains) {
        List<FileAction> fileActions = new ArrayList<>();
        domains.forEach(domain -> {
            if (!CollectionUtils.isEmpty(domain.getActions())) {
                fileActions.addAll(domain.getActions());
            }
        });
        this.fileActionEntityRepository.saveAll(this.fileActionEntityMapper.toEntity(fileActions));
        return super.saveAll(domains);
    }

    @Override
    public File getById(UUID id) {
        FileEntity found = this.jpaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(BadRequestError.FILE_NOT_FOUND));
        File domain = this.mapper.toDomain(found);
        domain.enrichActions(new ArrayList<>());
        return domain;
    }
}
