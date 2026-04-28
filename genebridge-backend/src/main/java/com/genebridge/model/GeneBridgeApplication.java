package com.genebridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class GeneBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneBridgeApplication.class, args);
        System.out.println("🧬 GeneBridge Backend Application Started Successfully!");
        System.out.println("🔗 API Endpoint: http://localhost:8080/api/annotate");
        System.out.println("💻 Server running on: http://localhost:8080");
    }

    /**
     * Configure RestTemplate with timeout settings for API calls
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}