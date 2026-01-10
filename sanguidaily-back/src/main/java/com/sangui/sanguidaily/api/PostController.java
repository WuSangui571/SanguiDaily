package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.PostCreateRequest;
import com.sangui.sanguidaily.model.Post;
import com.sangui.sanguidaily.service.PostService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
}
