package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.PostImageBatchRequest;
import com.sangui.sanguidaily.model.PostImage;
import com.sangui.sanguidaily.service.PostImageService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post-images")
public class PostImageController {

    private final PostImageService postImageService;

    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @GetMapping
    public List<PostImage> listImages(
        @RequestParam(value = "postId", required = false) Long postId,
        @RequestParam(value = "postIds", required = false) String postIds
    ) {
        List<Long> ids = resolvePostIds(postId, postIds);
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return postImageService.listByPostIds(ids);
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> createImages(@RequestBody PostImageBatchRequest request) {
        List<String> urls = request.imageUrls() == null ? List.of() : request.imageUrls();
        List<PostImage> images = new ArrayList<>();
        for (int i = 0; i < urls.size(); i += 1) {
            String url = urls.get(i);
            if (url == null || url.isBlank()) {
                continue;
            }
            images.add(new PostImage(
                null,
                request.postId(),
                url,
                i + 1,
                0,
                0,
                0,
                null,
                null
            ));
        }
        postImageService.createImages(images);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/replace")
    public ResponseEntity<Void> replaceImages(@RequestBody PostImageBatchRequest request) {
        List<String> urls = request.imageUrls() == null ? List.of() : request.imageUrls();
        List<PostImage> images = new ArrayList<>();
        for (int i = 0; i < urls.size(); i += 1) {
            String url = urls.get(i);
            if (url == null || url.isBlank()) {
                continue;
            }
            images.add(new PostImage(
                null,
                request.postId(),
                url,
                i + 1,
                0,
                0,
                0,
                null,
                null
            ));
        }
        postImageService.replaceImages(request.postId(), images);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Long> resolvePostIds(Long postId, String postIds) {
        List<Long> ids = new ArrayList<>();
        if (postId != null) {
            ids.add(postId);
        }
        if (postIds != null && !postIds.isBlank()) {
            String[] parts = postIds.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    ids.add(Long.parseLong(trimmed));
                }
            }
        }
        return ids;
    }
}
