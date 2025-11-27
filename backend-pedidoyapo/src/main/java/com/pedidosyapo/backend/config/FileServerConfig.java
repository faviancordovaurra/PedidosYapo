package com.pedidosyapo.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileServerConfig implements WebMvcConfigurer {

    @Value("${uploads.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // No-op: consolidated in WebConfig. Keeping class for compatibility.
    }
}
