package com.academo.controller.dtos.file;

public record DownloadGoogleFileDTO(
        String name,
        String mimeType,
        byte[] content
) {
}
