package com.onion.metro;

import com.onion.metro.client.MetroApiClient;
import com.onion.metro.model.MetroResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientTest {

    @Mock
    private WebClient webClient;


    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    }

    @Test
    void testPlayerClientCreation() {
        MetroApiClient client = new MetroApiClient(webClient);
        assertNotNull(client);
    }

    @Test
    void getTimesUsesPostRequest() {
        MetroApiClient client = new MetroApiClient(webClient);

        MetroResponse metroResponse = mock(MetroResponse.class);

        WebClient.RequestBodyUriSpec requestSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        lenient().when(webClient.post()).thenReturn(requestSpec);
        lenient().when(requestSpec.uri(any(String.class), any(Object.class)))
                .thenReturn(requestSpec);
        lenient().when(requestSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToFlux(MetroResponse.class))
                .thenReturn(Flux.just(metroResponse));

        assertNotNull(client.getTimes("581"));
    }




}