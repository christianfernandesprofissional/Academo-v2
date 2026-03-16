package com.academo.controller.dtos.file;

public record DownloadedFileDTO(
        String name,
        String mimeType,
        byte[] content
) {
}
