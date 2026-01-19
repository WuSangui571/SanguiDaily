package com.sangui.sanguidaily.dto;

import java.util.List;

public record UploadOrphanDeleteResult(
    List<String> deletedPaths,
    List<String> skippedPaths
) {}
