package ru.netology.cloudstorage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "cors")
@Data
public class WebConfig implements WebMvcConfigurer {

    String pathPattern;
    Boolean credentials;
    String origins;
    String methods;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(pathPattern)
                .allowCredentials(credentials)
                .allowedOrigins(origins)
                .allowedMethods(methods);
    }
}