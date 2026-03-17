package com.academo.controller.dtos.file;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public record DownloadS3FileDTO(
        String fileName,
        String mimeType,
        Long contentLength,
        ResponseInputStream<GetObjectResponse> response
) {
}
