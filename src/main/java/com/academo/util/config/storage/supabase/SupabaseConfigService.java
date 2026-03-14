package com.academo.util.config.storage.supabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class SupabaseConfigService {

    @Value("${storage.endpoint}")
    private String endpoint;
    @Value("${storage.region}")
    private String region;
    @Value("${storage.access-key}")
    private String accessKey;
    @Value("${storage.secret-key}")
    private String secretKey;

    private static final Logger log = LoggerFactory.getLogger(SupabaseConfigService.class);

    @Bean
    public S3Client s3Client() {
        log.info("Creating S3 client for endpoint: {}", endpoint);
        log.info("Creating S3 client for region: {}", region);
        log.info("Creating S3 client for access-key: {}", accessKey);
        log.info("Creating S3 client for secret-key: {}", secretKey);
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                )).build();
    }


}
