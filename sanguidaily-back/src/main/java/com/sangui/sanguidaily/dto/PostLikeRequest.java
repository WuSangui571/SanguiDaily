package com.sangui.sanguidaily.dto;

public record PostLikeRequest(
    Long postId,
    Long userId
) {}
