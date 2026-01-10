package com.sangui.sanguidaily.model;

import java.time.LocalDateTime;

public record PostLike(
    Long id,
    Long postId,
    Long userId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
