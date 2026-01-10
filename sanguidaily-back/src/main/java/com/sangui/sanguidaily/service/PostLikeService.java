package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.dto.PostLikeResult;
import com.sangui.sanguidaily.model.PostLike;
import com.sangui.sanguidaily.repository.PostLikeRepository;
import com.sangui.sanguidaily.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    public List<PostLike> listLikes(Long userId, List<Long> postIds) {
        return postLikeRepository.listByUserAndPostIds(userId, postIds);
    }

    @Transactional
    public PostLikeResult like(Long postId, Long userId) {
        boolean inserted = postLikeRepository.insertLike(postId, userId);
        if (inserted) {
            postRepository.incrementLikeCount(postId);
        }
        int likeCount = postRepository.findLikeCount(postId).orElse(0);
        return new PostLikeResult(true, likeCount);
    }

    @Transactional
    public PostLikeResult unlike(Long postId, Long userId) {
        boolean deleted = postLikeRepository.deleteLike(postId, userId);
        if (deleted) {
            postRepository.decrementLikeCount(postId);
        }
        int likeCount = postRepository.findLikeCount(postId).orElse(0);
        return new PostLikeResult(false, likeCount);
    }
}
