package com.sangui.sanguidaily.dto;

import com.sangui.sanguidaily.model.User;

public record AuthResponse(
    String token,
    User user
) {}
