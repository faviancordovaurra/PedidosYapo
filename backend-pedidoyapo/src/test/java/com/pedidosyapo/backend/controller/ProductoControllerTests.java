package com.pedidosyapo.backend.controller;

import com.pedidosyapo.backend.entity.Producto;
import com.pedidosyapo.backend.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Using TestConfiguration and Mockito beans instead of @MockBean
import static org.mockito.Mockito.when;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@TestPropertySource(properties = "uploads.path=target/test-uploads/")
public class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

        // ProductoService bean provided by TestConfiguration (mock)
        @Autowired
        private ProductoService productoService;

        @TestConfiguration
        static class Config {
                @Bean
                public ProductoService productoService() {
                        return Mockito.mock(ProductoService.class);
                }
        }

    @Test
    public void testUploadMultipleImages_ok() throws Exception {
        // Ensure directory
        Path dir = Paths.get("target/test-uploads/");
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Producto p = new Producto();
        p.setId(1L);

        when(productoService.findById(1L)).thenReturn(p);

        MockMultipartFile file1 = new MockMultipartFile("files", "a.png", "image/png",
                "hello".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "b.png", "image/png",
                "world".getBytes());

        mockMvc.perform(multipart("/api/productos/1/imagenes")
                        .file(file1)
                        .file(file2)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }
}
