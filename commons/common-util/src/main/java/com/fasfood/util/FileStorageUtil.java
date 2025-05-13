package com.fasfood.util;

import com.fasfood.common.error.BadRequestError;
import com.fasfood.common.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class FileStorageUtil {

    private static final String ROOT_DIR = "./data/uploads";

    private static final Path rootStoragePath = Paths.get(ROOT_DIR).toAbsolutePath().normalize();

    private static Path createDateBasedDirectory() throws IOException {
        LocalDate now = LocalDate.now();
        String datePath = String.format("%d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        Path directoryPath = rootStoragePath.resolve(datePath);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        return directoryPath;
    }

    public static void storeFile(Map<UUID, MultipartFile> files) throws IOException {
        for (Map.Entry<UUID, MultipartFile> entry : files.entrySet()) {
            Path targetLocation = createDateBasedDirectory()
                    .resolve(String.join(".", entry.getKey().toString(), getFileExtension(entry.getValue())));
            Files.copy(entry.getValue().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static String getFileExtension(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!originalFileName.contains(".")) {
            throw new ResponseException(BadRequestError.INVALID_INPUT, originalFileName);
        }
        return originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
    }

    public static String getFileType(MultipartFile file) {
        return Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");
    }

    public static Path getFilePath(LocalDate dateTime, String fileName) {
        String datePath = String.format("%d-%02d-%02d",
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth()
        );
        return rootStoragePath.resolve(Paths.get(datePath, fileName));
    }

    public static boolean isImageFile(String type) {
        return type.startsWith("image/");
    }

    public static Resource resizeImage(Path filePath, Integer width, Integer height, Double ratio) throws IOException {
        String mimeType = Files.probeContentType(filePath);
        log.error(mimeType);
        if (width == null && height == null && ratio == null || !mimeType.startsWith("image/")) {
            return new FileSystemResource(filePath.toFile());
        }
        BufferedImage originalImage = ImageIO.read(filePath.toFile());

        int newWidth = (width != null) ? width : originalImage.getWidth();
        int newHeight = (height != null) ? height : originalImage.getHeight();

        if (ratio != null) {
            newWidth = (int) (originalImage.getWidth() * ratio);
            newHeight = (int) (originalImage.getHeight() * ratio);
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        Path tempFilePath = Files.createTempFile("resized-", ".jpg");
        ImageIO.write(resizedImage, "jpg", tempFilePath.toFile());

        return new FileSystemResource(tempFilePath.toFile());
    }
}
