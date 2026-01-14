package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.ApiError;
import com.sangui.sanguidaily.dto.UploadResponse;
import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.UploadService;
import com.sangui.sanguidaily.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService uploadService;
    private final JwtService jwtService;
    private final UserService userService;
    private final String publicBase;

    public UploadController(
        UploadService uploadService,
        JwtService jwtService,
        UserService userService,
        @Value("${app.public-base:}") String publicBase
    ) {
        this.uploadService = uploadService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.publicBase = normalizePublicBase(publicBase);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestPart(value = "file", required = false) MultipartFile file,
        HttpServletRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError("仅作者可上传"));
        }
        try {
            UploadService.UploadResult result = uploadService.storeImage(file);
            String url = buildFileUrl(request, result.relativePath());
            return ResponseEntity.ok(new UploadResponse(url, result.filename(), result.size(), null));
        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage() == null ? "图片上传失败" : ex.getMessage();
            return ResponseEntity.badRequest().body(new ApiError(message));
        } catch (IllegalStateException ex) {
            String message = ex.getMessage() == null ? "保存文件失败" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(message));
        }
    }

    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestPart(value = "file", required = false) MultipartFile file,
        HttpServletRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError("仅作者可上传"));
        }
        try {
            UploadService.UploadResult result = uploadService.storeVideo(file);
            String url = buildFileUrl(request, result.relativePath());
            String coverUrl = result.coverRelativePath() == null
                ? null
                : buildFileUrl(request, result.coverRelativePath());
            return ResponseEntity.ok(new UploadResponse(url, result.filename(), result.size(), coverUrl));
        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage() == null ? "视频上传失败" : ex.getMessage();
            return ResponseEntity.badRequest().body(new ApiError(message));
        } catch (IllegalStateException ex) {
            String message = ex.getMessage() == null ? "保存文件失败" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(message));
        }
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

    private String buildFileUrl(HttpServletRequest request, String relativePath) {
        if (publicBase != null && !publicBase.isBlank()) {
            if (relativePath == null || relativePath.isBlank()) {
                return publicBase;
            }
            if (relativePath.startsWith("/")) {
                return publicBase + relativePath;
            }
            return publicBase + "/" + relativePath;
        }
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort =
            ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        StringBuilder builder = new StringBuilder();
        builder.append(scheme).append("://").append(host);
        if (!defaultPort) {
            builder.append(':').append(port);
        }
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isBlank()) {
            builder.append(contextPath);
        }
        if (relativePath == null || relativePath.isBlank()) {
            return builder.toString();
        }
        if (relativePath.startsWith("/")) {
            builder.append(relativePath);
        } else {
            builder.append('/').append(relativePath);
        }
        return builder.toString();
    }

    private String normalizePublicBase(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
