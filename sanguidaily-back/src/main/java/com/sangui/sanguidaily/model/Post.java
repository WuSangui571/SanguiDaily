package com.sangui.sanguidaily.model;

import java.time.LocalDateTime;

public record Post(
    Long id,
    Long authorId,
    Integer type,
    String contentText,
    String linkUrl,
    String linkTitle,
    String linkCoverUrl,
    String linkSiteName,
    String videoUrl,
    String videoCoverUrl,
    Integer status,
    Integer isPinned,
    LocalDateTime pinnedAt,
    Integer likeCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt
) {}
