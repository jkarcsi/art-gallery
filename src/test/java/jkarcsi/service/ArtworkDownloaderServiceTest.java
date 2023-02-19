package jkarcsi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.function.Function;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import jkarcsi.configuration.ArtworkDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@TestWithResources
class ArtworkDownloaderServiceTest {

    @InjectMocks
    private ArtworkDownloaderService artworkDownloaderService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    ArtworkDeserializer artworkDeserializer;

    @GivenTextResource("txt/ArtworkEscapedString.txt")
    String artworkString;

    @GivenTextResource("txt/ArtworkPageEscapedString.txt")
    String artworkPageString;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void test_getArtworkResponse_successful() throws IOException {

        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> responseEntity = new ResponseEntity<>(artworkString, headers, HttpStatus.OK);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));
        final String result = artworkDownloaderService.getArtworkResponse("129884").getBody();

        assertThat(result).isEqualTo(artworkString);
    }

    @Test
    void test_getPaginatedArtworkResponse_successful() throws IOException {

        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> responseEntity = new ResponseEntity<>(artworkPageString, headers, HttpStatus.OK);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(responseEntity));
        final String result = artworkDownloaderService.getPaginatedArtworkResponse(1, 5).getBody();

        assertThat(result).isEqualTo(artworkPageString);
    }
}