package com.sangui.sanguidaily.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "m4v", "webm", "avi", "mkv");
    private static final String VIDEO_COVER_CATEGORY = "video-covers";

    private final Path uploadRoot;
    private final String ffmpegPath;

    public UploadService(
        @Value("${app.upload-root:uploads}") String uploadRoot,
        @Value("${app.ffmpeg-path:ffmpeg}") String ffmpegPath
    ) {
        this.uploadRoot = Paths.get(uploadRoot).toAbsolutePath().normalize();
        this.ffmpegPath = sanitizeExecutable(ffmpegPath);
    }

    public UploadResult storeImage(MultipartFile file) {
        StoredFile stored = store(file, "images", IMAGE_EXTS, "图片");
        return new UploadResult(stored.relativePath, stored.filename, stored.size, null);
    }

    public UploadResult storeVideo(MultipartFile file) {
        StoredFile stored = store(file, "videos", VIDEO_EXTS, "视频");
        String coverRelative = generateVideoCover(stored.absolutePath, stored.dateFolder);
        return new UploadResult(stored.relativePath, stored.filename, stored.size, coverRelative);
    }

    private StoredFile store(
        MultipartFile file,
        String category,
        Set<String> allowedExts,
        String label
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(label + "文件不能为空");
        }
        String extension = resolveExtension(file, allowedExts);
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
        Path absolutePath = targetDir.resolve(filename);
        return new StoredFile(relativePath, filename, file.getSize(), absolutePath, dateFolder);
    }

    private String generateVideoCover(Path videoPath, String dateFolder) {
        if (videoPath == null || !Files.exists(videoPath)) {
            return null;
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        Path coverDir = uploadRoot.resolve(VIDEO_COVER_CATEGORY).resolve(dateFolder);
        Path coverPath = coverDir.resolve(filename);
        try {
            Files.createDirectories(coverDir);
            List<String> command = new ArrayList<>();
            command.add(ffmpegPath);
            command.add("-y");
            command.add("-hide_banner");
            command.add("-loglevel");
            command.add("error");
            command.add("-ss");
            command.add("00:00:00");
            command.add("-i");
            command.add(videoPath.toString());
            command.add("-frames:v");
            command.add("1");
            command.add("-q:v");
            command.add("2");
            command.add(coverPath.toString());
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            String output = readProcessOutput(process.getInputStream());
            boolean finished = process.waitFor(12, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.out.println("ffmpeg cover timeout: " + output);
                return null;
            }
            if (process.exitValue() != 0 || !Files.exists(coverPath)) {
                if (!output.isBlank()) {
                    System.out.println("ffmpeg cover failed: " + output);
                }
                return null;
            }
            return "/uploads/" + VIDEO_COVER_CATEGORY + "/" + dateFolder + "/" + filename;
        } catch (Exception ex) {
            System.out.println("ffmpeg cover exception: " + ex.getMessage());
            return null;
        }
    }

    private String sanitizeExecutable(String value) {
        if (value == null || value.isBlank()) {
            return "ffmpeg";
        }
        String trimmed = value.trim();
        if ((trimmed.startsWith("\"") && trimmed.endsWith("\""))
            || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private String readProcessOutput(InputStream input) {
        if (input == null) return "";
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            char[] buffer = new char[512];
            int count;
            while ((count = reader.read(buffer)) > 0) {
                builder.append(buffer, 0, count);
                if (builder.length() > 2000) {
                    break;
                }
            }
        } catch (IOException ex) {
            return "";
        }
        return builder.toString().trim();
    }

    private String resolveExtension(MultipartFile file, Set<String> allowedExts) {
        if (file == null) {
            return "";
        }
        String name = file.getOriginalFilename();
        String extension = resolveExtensionFromName(name);
        if (!extension.isEmpty() && allowedExts.contains(extension)) {
            return extension;
        }
        String contentType = file.getContentType();
        String mapped = resolveExtensionFromContentType(contentType);
        if (!mapped.isEmpty() && allowedExts.contains(mapped)) {
            return mapped;
        }
        if (contentType != null) {
            String lower = contentType.toLowerCase(Locale.ROOT);
            if (lower.startsWith("image/") && allowedExts.contains("jpg")) {
                return "jpg";
            }
            if (lower.startsWith("video/") && allowedExts.contains("mp4")) {
                return "mp4";
            }
        }
        return extension;
    }

    private String resolveExtensionFromName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        int index = name.lastIndexOf('.');
        if (index < 0 || index == name.length() - 1) {
            return "";
        }
        return name.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private String resolveExtensionFromContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "";
        }
        String lower = contentType.toLowerCase(Locale.ROOT);
        if (lower.contains("jpeg")) return "jpg";
        if (lower.contains("png")) return "png";
        if (lower.contains("gif")) return "gif";
        if (lower.contains("webp")) return "webp";
        if (lower.contains("bmp")) return "bmp";
        if (lower.contains("mp4")) return "mp4";
        if (lower.contains("quicktime")) return "mov";
        if (lower.contains("webm")) return "webm";
        if (lower.contains("x-msvideo")) return "avi";
        if (lower.contains("x-matroska")) return "mkv";
        return "";
    }

    private record StoredFile(String relativePath, String filename, long size, Path absolutePath, String dateFolder) {}

    public record UploadResult(String relativePath, String filename, long size, String coverRelativePath) {}
}
