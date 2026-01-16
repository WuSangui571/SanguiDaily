package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.dto.PostCreateRequest;
import com.sangui.sanguidaily.dto.PostUpdateRequest;
import com.sangui.sanguidaily.model.Post;
import com.sangui.sanguidaily.model.PostImage;
import com.sangui.sanguidaily.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostImageService postImageService;

    public PostService(PostRepository postRepository, PostImageService postImageService) {
        this.postRepository = postRepository;
        this.postImageService = postImageService;
    }

    public List<Post> listFeedPosts(String role, Integer type) {
        return postRepository.listFeedPosts(role, type);
    }

    public Optional<Post> findVisibleById(Long id, String role) {
        return postRepository.findVisibleById(id, role);
    }

    public Post createPost(PostCreateRequest request) {
        return postRepository.insert(request);
    }

    public Optional<Post> updatePost(Long id, PostUpdateRequest request) {
        Optional<Post> updated = postRepository.updatePost(id, request);
        if (updated.isPresent() && request.type() != null && request.type() == 1 && request.imageUrls() != null) {
            postImageService.replaceImages(id, buildImages(id, request.imageUrls()));
        }
        return updated;
    }

    public Optional<Post> updatePinned(Long id, boolean pinned) {
        return postRepository.updatePinned(id, pinned);
    }

    public Optional<Post> updateStatus(Long id, Integer status) {
        return postRepository.updateStatus(id, status);
    }

    public List<Post> listDeletedPosts() {
        return postRepository.listDeletedPosts();
    }

    public Optional<Post> softDelete(Long id) {
        return postRepository.softDelete(id);
    }

    public Optional<Post> restore(Long id) {
        return postRepository.restore(id);
    }

    private List<PostImage> buildImages(Long postId, List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return List.of();
        }
        List<PostImage> images = new java.util.ArrayList<>();
        for (int i = 0; i < urls.size(); i += 1) {
            String url = urls.get(i);
            if (url == null || url.isBlank()) {
                continue;
            }
            images.add(new PostImage(
                null,
                postId,
                url,
                i + 1,
                0,
                0,
                0,
                null,
                null
            ));
        }
        return images;
    }
}
