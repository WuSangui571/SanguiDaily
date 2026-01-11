package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.model.PostImage;
import com.sangui.sanguidaily.repository.PostImageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PostImageService {

    private final PostImageRepository postImageRepository;

    public PostImageService(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    public List<PostImage> listByPostIds(List<Long> postIds) {
        return postImageRepository.listByPostIds(postIds);
    }

    public void createImages(List<PostImage> images) {
        postImageRepository.insertBatch(images);
    }

    public void replaceImages(Long postId, List<PostImage> images) {
        if (postId == null) {
            return;
        }
        postImageRepository.deleteByPostId(postId);
        postImageRepository.insertBatch(images);
    }
}
