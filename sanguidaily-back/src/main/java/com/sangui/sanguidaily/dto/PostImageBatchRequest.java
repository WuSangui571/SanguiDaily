package com.sangui.sanguidaily.dto;

import java.util.List;

public record PostImageBatchRequest(
    Long postId,
    List<String> imageUrls
) {}
