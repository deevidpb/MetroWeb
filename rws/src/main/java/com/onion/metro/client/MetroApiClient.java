package com.onion.metro.client;


import com.onion.metro.model.MetroResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MetroApiClient {
    private final WebClient webClient;

    public MetroApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public MetroResponse getTimes(String stationId) {
        return webClient
                .post()
                .uri("/es/metro_next_trains/modal/{id}", stationId)
                .retrieve()
                .bodyToFlux(MetroResponse.class)
                .next()
                .block();
    }
}
