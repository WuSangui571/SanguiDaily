package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.repository.PostImageRepository;
import com.sangui.sanguidaily.repository.PostRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadCleanupService {

    private static final String UPLOAD_PREFIX = "/uploads/";
    private static final Set<String> CATEGORIES = Set.of("images", "videos", "video-covers");

    private final Path uploadRoot;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public UploadCleanupService(
        @Value("${app.upload-root:uploads}") String uploadRoot,
        PostRepository postRepository,
        PostImageRepository postImageRepository
    ) {
        this.uploadRoot = Paths.get(uploadRoot).toAbsolutePath().normalize();
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
    }

    public List<OrphanFile> scanOrphans(Set<String> referencedPaths) {
        Set<String> refs = referencedPaths == null ? Set.of() : referencedPaths;
        List<OrphanFile> orphans = new ArrayList<>();
        for (String category : CATEGORIES) {
            Path categoryPath = uploadRoot.resolve(category);
            if (!Files.isDirectory(categoryPath)) {
                continue;
            }
            try (Stream<Path> stream = Files.walk(categoryPath)) {
                stream.filter(Files::isRegularFile).forEach((path) -> {
                    String relative = uploadRoot.relativize(path).toString().replace('\\', '/');
                    String uploadPath = UPLOAD_PREFIX + relative;
                    if (!refs.contains(uploadPath)) {
                        OrphanFile file = new OrphanFile(
                            uploadPath,
                            safeFileSize(path),
                            safeLastModified(path),
                            category
                        );
                        orphans.add(file);
                    }
                });
            } catch (IOException ex) {
                // ignore scan errors for missing directories
            }
        }
        return orphans;
    }

    public Set<String> loadReferencedPaths() {
        Set<String> refs = new HashSet<>();
        if (postImageRepository != null) {
            List<String> images = postImageRepository.listAllImageUrls();
            collectPaths(refs, images);
        }
        if (postRepository != null) {
            List<PostRepository.PostMediaRef> medias = postRepository.listAllMediaRefs();
            for (PostRepository.PostMediaRef media : medias) {
                collectPath(refs, media.linkCoverUrl());
                collectPath(refs, media.videoUrl());
                collectPath(refs, media.videoCoverUrl());
            }
        }
        return refs;
    }

    public boolean deleteByUploadPath(String uploadPath) {
        String normalized = normalizeToUploadPath(uploadPath);
        if (normalized == null || !normalized.startsWith(UPLOAD_PREFIX)) {
            return false;
        }
        String relative = normalized.substring(UPLOAD_PREFIX.length());
        Path target = uploadRoot.resolve(relative).normalize();
        if (!target.startsWith(uploadRoot)) {
            return false;
        }
        if (!Files.isRegularFile(target)) {
            return false;
        }
        try {
            return Files.deleteIfExists(target);
        } catch (IOException ex) {
            return false;
        }
    }

    public static String normalizeToUploadPath(String url) {
        if (url == null) {
            return null;
        }
        String trimmed = url.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        int index = trimmed.indexOf(UPLOAD_PREFIX);
        if (index < 0) {
            return null;
        }
        String path = trimmed.substring(index);
        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            path = path.substring(0, queryIndex);
        }
        int hashIndex = path.indexOf('#');
        if (hashIndex >= 0) {
            path = path.substring(0, hashIndex);
        }
        return path;
    }

    private void collectPaths(Set<String> target, List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        for (String url : urls) {
            collectPath(target, url);
        }
    }

    private void collectPath(Set<String> target, String url) {
        String normalized = normalizeToUploadPath(url);
        if (normalized != null) {
            target.add(normalized);
        }
    }

    private long safeFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ex) {
            return 0L;
        }
    }

    private long safeLastModified(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException ex) {
            return 0L;
        }
    }

    public record OrphanFile(String path, long sizeBytes, long lastModifiedAt, String category) {
        public String category() {
            if (category == null) {
                return "unknown";
            }
            return category.toLowerCase(Locale.ROOT);
        }
    }
}
