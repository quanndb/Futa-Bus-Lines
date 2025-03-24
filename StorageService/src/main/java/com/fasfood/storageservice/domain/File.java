package com.fasfood.storageservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
import com.fasfood.storageservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.util.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class File extends AuditableDomain {
    private UUID id;
    private UUID ownerId;
    private String name;
    private String path;
    private String type;
    private String extension;
    private boolean sharing;
    private long size;
    private List<FileAction> actions;
    private boolean deleted;

    private static final Set<String> BLACK_LIST_FILES = Set.of("exe", "bat");

    public File(MultipartFile file, UUID ownerId, Boolean sharing) {
        this.id = IdUtils.nextId();
        this.ownerId = ownerId;
        this.name = validateFileType(file);
        this.extension = getFileExtension(this.name);
        this.path = this.id.toString();
        this.type = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
        this.sharing = sharing;
        this.size = file.getSize();
        this.deleted = false;
        this.actions = new ArrayList<>();
        this.addAction(FileActionEnum.UPLOAD);
    }

    public void download() {
        this.addAction(FileActionEnum.DOWNLOAD);
    }

    private void addAction(FileActionEnum actionType) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        actions.add(new FileAction(id, ownerId, actionType));
    }

    private static String validateFileType(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (!originalFileName.contains(".")) {
            throw new ResponseException(BadRequestError.INVALID_FILE_NAME, originalFileName);
        }

        String fileExtension = getFileExtension(originalFileName);
        if (BLACK_LIST_FILES.contains(fileExtension)) {
            throw new ResponseException(BadRequestError.UN_SUPPORT_FILE_TYPE, fileExtension);
        }
        return originalFileName;
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    public void enrichActions(List<FileAction> actions) {
        this.actions = actions;
    }

    public void delete() {
        this.deleted = Boolean.TRUE;
    }
}
