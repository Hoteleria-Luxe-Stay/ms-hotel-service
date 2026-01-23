package com.hotel.hotel.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${application.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .collect(Collectors.toList());
        config.setAllowedOrigins(origins.isEmpty() ? List.of("http://localhost:4200") : origins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        config.setAllowCredentials(!config.getAllowedOrigins().contains("*"));
        config.setMaxAge(3600L);

        CorsRegistration corsRegistration = registry.addMapping("/**");
        corsRegistration.allowedOrigins(config.getAllowedOrigins().toArray(String[]::new));
        corsRegistration.allowedMethods(config.getAllowedMethods().toArray(String[]::new));
        corsRegistration.allowedHeaders(config.getAllowedHeaders().toArray(String[]::new));
        corsRegistration.exposedHeaders(config.getExposedHeaders().toArray(String[]::new));
        corsRegistration.allowCredentials(Boolean.TRUE.equals(config.getAllowCredentials()));
        corsRegistration.maxAge(config.getMaxAge());
    }
}
