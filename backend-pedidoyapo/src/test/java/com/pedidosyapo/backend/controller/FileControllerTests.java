package com.pedidosyapo.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Using TestConfiguration for mock beans instead of @MockBean
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// no-op for now (kept for future stubbing)

@WebMvcTest(FileController.class)
@TestPropertySource(properties = "uploads.path=target/test-uploads/")
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // ProductoService bean provided by TestConfiguration (mock)

    @TestConfiguration
    static class Config {
        @Bean
        public com.pedidosyapo.backend.service.ProductoService productoService() {
            return Mockito.mock(com.pedidosyapo.backend.service.ProductoService.class);
        }
    }

    @Test
    public void testUploadImage_multipart_ok() throws Exception {
        // Ensure target folder exists in the test environment
        Path dir = Paths.get("target/test-uploads/");
        if (!Files.exists(dir)) Files.createDirectories(dir);
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                "fake-image-content".getBytes());

        mockMvc.perform(multipart("/api/uploads/imagen")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }
}
