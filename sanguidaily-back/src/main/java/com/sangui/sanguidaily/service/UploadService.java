package com.sangui.sanguidaily.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "m4v", "webm", "avi", "mkv");

    private final Path uploadRoot;

    public UploadService(@Value("${app.upload-root:uploads}") String uploadRoot) {
        this.uploadRoot = Paths.get(uploadRoot).toAbsolutePath().normalize();
    }

    public UploadResult storeImage(MultipartFile file) {
        return store(file, "images", IMAGE_EXTS, "图片");
    }

    public UploadResult storeVideo(MultipartFile file) {
        return store(file, "videos", VIDEO_EXTS, "视频");
    }

    private UploadResult store(
        MultipartFile file,
        String category,
        Set<String> allowedExts,
        String label
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(label + "文件不能为空");
        }
        String extension = resolveExtension(file.getOriginalFilename());
        if (extension.isEmpty() || !allowedExts.contains(extension)) {
            throw new IllegalArgumentException(label + "类型不支持");
        }
        String dateFolder = LocalDate.now().format(DATE_FORMAT);
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        Path targetDir = uploadRoot.resolve(category).resolve(dateFolder);
        try {
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(filename);
            file.transferTo(targetFile.toFile());
        } catch (IOException ex) {
            throw new IllegalStateException("保存文件失败", ex);
        }
        String relativePath = "/uploads/" + category + "/" + dateFolder + "/" + filename;
        return new UploadResult(relativePath, filename, file.getSize());
    }

    private String resolveExtension(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        int index = name.lastIndexOf('.');
        if (index < 0 || index == name.length() - 1) {
            return "";
        }
        return name.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    public record UploadResult(String relativePath, String filename, long size) {}
}
