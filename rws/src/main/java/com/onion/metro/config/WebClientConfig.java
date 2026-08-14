package com.onion.metro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${metro.base-url}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("origin", "https://www.metromadrid.es")
                .defaultHeader(
                        "User-Agent", "curl/8.0")
                .filter((request, next) -> next.exchange(request))
                .build();
    }
}
