package com.sangui.sanguidaily.dto;

import java.util.List;

public record PostUpdateRequest(
    Integer type,
    String contentText,
    String linkUrl,
    String linkTitle,
    String linkCoverUrl,
    String linkSiteName,
    String videoUrl,
    String videoCoverUrl,
    Integer status,
    List<String> imageUrls
) {}
