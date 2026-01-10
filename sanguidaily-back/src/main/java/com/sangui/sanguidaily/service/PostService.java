package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.dto.PostCreateRequest;
import com.sangui.sanguidaily.model.Post;
import com.sangui.sanguidaily.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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
}
