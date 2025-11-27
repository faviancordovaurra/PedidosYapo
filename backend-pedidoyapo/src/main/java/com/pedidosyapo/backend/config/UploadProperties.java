package com.pedidosyapo.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "uploads")
public class UploadProperties {

    private String path;
    private long maxSizeBytes = 10485760; // 10MB default

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getMaxSizeBytes() {
        return maxSizeBytes;
    }
    public void setMaxSizeBytes(long maxSizeBytes) {
        this.maxSizeBytes = maxSizeBytes;
    }
}
