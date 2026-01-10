package com.sangui.sanguidaily.dto;

public record PostCreateRequest(
    Long authorId,
    Integer type,
    String contentText,
    String linkUrl,
    String linkTitle,
    String linkCoverUrl,
    String linkSiteName,
    String videoUrl,
    String videoCoverUrl,
    Integer status
) {}
