package com.sangui.sanguidaily.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UploadCleanupServiceTests {

    @TempDir
    Path tempDir;

    @Test
    void scanOrphansWithReferences_returnsOnlyUnreferenced() throws Exception {
        Files.createDirectories(tempDir.resolve("images/20260114"));
        Path keep = tempDir.resolve("images/20260114/keep.png");
        Path orphan = tempDir.resolve("images/20260114/orphan.png");
        Files.writeString(keep, "ok");
        Files.writeString(orphan, "x");

        UploadCleanupService service = new UploadCleanupService(tempDir.toString(), null, null);
        Set<String> refs = Set.of("/uploads/images/20260114/keep.png");

        List<UploadCleanupService.OrphanFile> result = service.scanOrphans(refs);
        assertEquals(1, result.size());
        assertEquals("/uploads/images/20260114/orphan.png", result.get(0).path());
    }

    @Test
    void normalizeUrl_extractsUploadsPath() {
        String url = "https://sangui.top/sanguidaily/uploads/images/20260114/a.png";
        assertEquals("/uploads/images/20260114/a.png", UploadCleanupService.normalizeToUploadPath(url));
    }
}
