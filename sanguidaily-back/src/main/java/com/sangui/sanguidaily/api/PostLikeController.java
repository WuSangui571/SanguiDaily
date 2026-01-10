package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.PostLikeRequest;
import com.sangui.sanguidaily.dto.PostLikeResult;
import com.sangui.sanguidaily.model.PostLike;
import com.sangui.sanguidaily.service.PostLikeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post-likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @GetMapping
    public List<PostLike> listLikes(
        @RequestParam("userId") Long userId,
        @RequestParam(value = "postIds", required = false) String postIds
    ) {
        List<Long> ids = resolvePostIds(postIds);
        return postLikeService.listLikes(userId, ids);
    }

    @PostMapping
    public ResponseEntity<PostLikeResult> like(@RequestBody PostLikeRequest request) {
        PostLikeResult result = postLikeService.like(request.postId(), request.userId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<PostLikeResult> unlike(
        @RequestParam("postId") Long postId,
        @RequestParam("userId") Long userId
    ) {
        PostLikeResult result = postLikeService.unlike(postId, userId);
        return ResponseEntity.ok(result);
    }

    private List<Long> resolvePostIds(String postIds) {
        if (postIds == null || postIds.isBlank()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();
        String[] parts = postIds.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                ids.add(Long.parseLong(trimmed));
            }
        }
        return ids;
    }
}
