package com.sangui.sanguidaily.dto;

public record UploadOrphanFile(
    String path,
    String category,
    long sizeBytes,
    long lastModifiedAt
) {}
