package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.UploadOrphanDeleteRequest;
import com.sangui.sanguidaily.dto.UploadOrphanDeleteResult;
import com.sangui.sanguidaily.dto.UploadOrphanFile;
import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.UploadCleanupService;
import com.sangui.sanguidaily.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uploads/orphans")
public class UploadCleanupController {

    private final UploadCleanupService uploadCleanupService;
    private final JwtService jwtService;
    private final UserService userService;

    public UploadCleanupController(
        UploadCleanupService uploadCleanupService,
        JwtService jwtService,
        UserService userService
    ) {
        this.uploadCleanupService = uploadCleanupService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UploadOrphanFile>> listOrphans(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }
        Set<String> refs = uploadCleanupService.loadReferencedPaths();
        List<UploadCleanupService.OrphanFile> orphans = uploadCleanupService.scanOrphans(refs);
        List<UploadOrphanFile> response = new ArrayList<>();
        for (UploadCleanupService.OrphanFile orphan : orphans) {
            response.add(new UploadOrphanFile(
                orphan.path(),
                orphan.category(),
                orphan.sizeBytes(),
                orphan.lastModifiedAt()
            ));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<UploadOrphanDeleteResult> deleteOrphans(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestBody(required = false) UploadOrphanDeleteRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new UploadOrphanDeleteResult(List.of(), List.of()));
        }
        List<String> requested = request == null || request.paths() == null
            ? List.of()
            : request.paths();
        Set<String> refs = uploadCleanupService.loadReferencedPaths();
        List<UploadCleanupService.OrphanFile> orphans = uploadCleanupService.scanOrphans(refs);
        Set<String> orphanPaths = new HashSet<>();
        for (UploadCleanupService.OrphanFile orphan : orphans) {
            orphanPaths.add(orphan.path());
        }
        List<String> deleted = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        for (String path : requested) {
            String normalized = UploadCleanupService.normalizeToUploadPath(path);
            if (normalized == null || !orphanPaths.contains(normalized)) {
                skipped.add(path);
                continue;
            }
            boolean ok = uploadCleanupService.deleteByUploadPath(normalized);
            if (ok) {
                deleted.add(normalized);
            } else {
                skipped.add(normalized);
            }
        }
        return ResponseEntity.ok(new UploadOrphanDeleteResult(deleted, skipped));
    }

    private boolean isOwner(String authorization) {
        Optional<JwtService.JwtUser> jwtUser = jwtService.parseToken(authorization);
        if (jwtUser.isEmpty()) {
            return false;
        }
        JwtService.JwtUser payload = jwtUser.get();
        Optional<User> user = userService.getCurrentUser(payload.userId(), payload.openid());
        return user.isPresent() && "OWNER".equals(user.get().role());
    }
}
