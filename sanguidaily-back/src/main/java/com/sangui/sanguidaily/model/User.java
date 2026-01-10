package com.sangui.sanguidaily.model;

import java.time.LocalDateTime;

public record User(
    Long id,
    String openid,
    String role,
    String nickname,
    String avatarUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime lastLoginAt
) {}
