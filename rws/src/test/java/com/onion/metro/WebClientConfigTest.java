package com.onion.metro;

import com.onion.metro.config.WebClientConfig;
import io.netty.handler.timeout.ReadTimeoutException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebClientConfigTest {

    private final WebClientConfig config = new WebClientConfig();
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @ParameterizedTest
    @CsvSource({
            "https://api.spotify.com/v1, https://www.metromadrid.es, curl/8.0",
            "https://custom.api.com/v1, https://custom.origin.com, Mozilla/5.0",
            "http://localhost:8080, http://localhost:3000, Test-Agent"
    })
    void testWebClientCreation(
            String baseUrl,
            String origin,
            String userAgent
    ) {
        WebClient webClient = config.webClient(
                baseUrl,
                origin,
                userAgent
        );

        assertNotNull(webClient);
    }

    @Test
    void shouldTriggerReadTimeoutHandlerWhenResponseIsDelayed() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("{\"message\":\"delayed\"}")
                        .setBodyDelay(11, TimeUnit.SECONDS)
        );

        String mockUrl = mockWebServer.url("/test").toString();
        WebClient webClient = config.webClient(
                mockUrl,
                "http://localhost",
                "Test-Agent"
        );
        Mono<String> responseMono = webClient.get()
                .retrieve()
                .bodyToMono(String.class);

        Duration delay = Duration.ofSeconds(12);

        WebClientResponseException exception = assertThrows(
                WebClientResponseException.class,
                () -> responseMono.block(delay)
        );
        assertInstanceOf(ReadTimeoutException.class, exception.getCause());
    }
}