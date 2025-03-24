package com.fasfood.storageservice.application.service.cmd.impl;

import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.storageservice.application.dto.mapper.FileDTOMapper;
import com.fasfood.storageservice.application.service.cmd.FileCommandService;
import com.fasfood.storageservice.domain.File;
import com.fasfood.storageservice.domain.repository.FileRepository;
import com.fasfood.util.FileStorageUtil;
import com.fasfood.web.support.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileCommandServiceImpl implements FileCommandService {

    private final FileRepository fileRepository;
    private final FileDTOMapper fileDTOMapper;

    @Override
    @Transactional
    public List<FileResponse> upload(List<MultipartFile> files, Boolean sharing) throws IOException {
        List<File> fileList = new ArrayList<>();
        Map<UUID, MultipartFile> fileMap = new HashMap<>();
        UUID ownerId = SecurityUtils.getUserId();
        for (MultipartFile file : files) {
            File newFile = new File(file, ownerId, sharing);
            fileList.add(newFile);
            fileMap.put(newFile.getId(), file);
        }
        FileStorageUtil.storeFile(fileMap);
        this.fileRepository.saveAll(fileList);
        return this.fileDTOMapper.domainToDTO(fileList);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        File found = this.fileRepository.getById(id);
        found.delete();
        this.fileDTOMapper.domainToDTO(found);
    }
}
