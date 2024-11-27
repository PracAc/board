package org.oz.basic_board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAPIConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("HEAD","GET","POST","PUT","DELETE","OPTIONS")
                .maxAge(300)
                .allowedHeaders("Authorization", "Cache-Control","Content-Type");

    }
}