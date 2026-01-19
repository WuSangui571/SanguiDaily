package com.sangui.sanguidaily.dto;

import java.util.List;

public record UploadOrphanDeleteRequest(
    List<String> paths
) {}
