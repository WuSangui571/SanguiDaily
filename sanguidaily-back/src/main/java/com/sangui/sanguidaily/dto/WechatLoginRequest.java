package com.sangui.sanguidaily.dto;

public record WechatLoginRequest(
    String code,
    String nickname,
    String avatarUrl
) {}
