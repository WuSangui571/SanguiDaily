package com.sangui.sanguidaily.dto;

public record UploadResponse(
    String url,
    String filename,
    Long size,
    String coverUrl
) {}
