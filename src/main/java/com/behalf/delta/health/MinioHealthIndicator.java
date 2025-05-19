package com.behalf.delta.health;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MinioHealthIndicator implements HealthIndicator {

    private final MinioClient minioClient;

    public MinioHealthIndicator(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public Health health() {
        try {
            // Simple call to check availability, e.g., list buckets
            minioClient.listBuckets();
            return Health.up().withDetail("MinIO", "Available").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("MinIO", "Unavailable").build();
        }
    }
}
