package com.sangui.sanguidaily.model;

import java.time.LocalDateTime;

public record PostImage(
    Long id,
    Long postId,
    String imageUrl,
    Integer sortOrder,
    Integer width,
    Integer height,
    Integer sizeBytes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
