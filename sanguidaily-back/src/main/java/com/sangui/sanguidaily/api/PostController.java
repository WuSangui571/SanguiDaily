package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.PostCreateRequest;
import com.sangui.sanguidaily.dto.PostPinRequest;
import com.sangui.sanguidaily.dto.PostStatusRequest;
import com.sangui.sanguidaily.dto.PostUpdateRequest;
import com.sangui.sanguidaily.model.Post;
import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.PostService;
import com.sangui.sanguidaily.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;
    private final UserService userService;

    public PostController(PostService postService, JwtService jwtService, UserService userService) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping
    public List<Post> listPosts(
        @RequestParam(value = "role", required = false) String role,
        @RequestParam(value = "type", required = false) Integer type
    ) {
        return postService.listFeedPosts(role, type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(
        @PathVariable("id") Long id,
        @RequestParam(value = "role", required = false) String role
    ) {
        Optional<Post> post = postService.findVisibleById(id, role);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequest request) {
        Post created = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable("id") Long id,
        @RequestBody PostUpdateRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Post> updated = postService.updatePost(id, request);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<Post> togglePin(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable("id") Long id,
        @RequestBody PostPinRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean pinned = request != null && Boolean.TRUE.equals(request.pinned());
        Optional<Post> updated = postService.updatePinned(id, pinned);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Post> toggleStatus(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable("id") Long id,
        @RequestBody PostStatusRequest request
    ) {
        if (!isOwner(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Integer status = request == null ? null : request.status();
        if (status == null || (status != 0 && status != 1)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Post> updated = postService.updateStatus(id, status);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
