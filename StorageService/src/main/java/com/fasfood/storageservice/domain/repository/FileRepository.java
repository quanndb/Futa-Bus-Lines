package com.fasfood.storageservice.domain.repository;

import com.fasfood.storageservice.domain.File;
import com.fasfood.web.support.DomainRepository;

import java.util.UUID;

public interface FileRepository extends DomainRepository<File, UUID> {
}
