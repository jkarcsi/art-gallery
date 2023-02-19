package jkarcsi.service;

import static java.util.Objects.requireNonNull;
import static jkarcsi.utils.constants.ExternalRequestParams.ARTIC_PARAMS;
import static jkarcsi.utils.constants.GalleryMessages.ARTWORK_NOT_FOUND;
import static jkarcsi.utils.constants.GalleryMessages.WEBCLIENT_FORBIDDEN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import jkarcsi.dto.gallery.ArtworkPage;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.utils.exception.CustomGalleryException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArtworkDownloaderService {

    @Autowired
    private WebClient webClient;

    public Artwork getArtwork(String id) {
        final String body = getArtworkResponse(id).getBody();
        final Artwork response;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            response = mapper.readValue(body, Artwork.class);
        } catch (JsonProcessingException e) {
            throw new CustomGalleryException(BAD_REQUEST, e.getMessage());
        }
        return requireNonNull(response);
    }

    public ArtworkPage getArtworksPaginated(final Integer page, final Integer limit) {
        final String body = getPaginatedArtworkResponse(page, limit).getBody();
        final ArtworkPage response;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            response = mapper.readValue(body, ArtworkPage.class);
        } catch (IOException e) {
            throw new CustomGalleryException(BAD_REQUEST, e.getMessage());
        }
        return requireNonNull(response);
    }

    public ResponseEntity<String> getArtworkResponse(String id) {
        final ResponseEntity<String> response =
                webClient.get().uri(uriBuilder -> uriBuilder.path("/{id}")
                                .queryParam("fields", StringUtils.join(ARTIC_PARAMS, ","))
                                .build(id)).retrieve()
                        .onStatus(httpStatus -> httpStatus.equals(FORBIDDEN), articApiResponse -> {
                            throw new CustomGalleryException(FORBIDDEN, WEBCLIENT_FORBIDDEN);
                        }).onStatus(httpStatus -> httpStatus.equals(NOT_FOUND), articApiResponse -> {
                            throw new CustomGalleryException(NOT_FOUND, ARTWORK_NOT_FOUND);
                        }).toEntity(String.class).block();
        return requireNonNull(response);
    }

    public ResponseEntity<String> getPaginatedArtworkResponse(final Integer page, final Integer limit) {
        final ResponseEntity<String> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("page", "{page}")
                        .queryParam("limit", "{limit}")
                        .queryParam("fields", StringUtils.join(ARTIC_PARAMS, ","))
                        .build(page, limit)
                )
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(FORBIDDEN), articApiResponse -> {
                    throw new CustomGalleryException(FORBIDDEN, WEBCLIENT_FORBIDDEN);})
                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND), articApiResponse -> {
                    throw new CustomGalleryException(NOT_FOUND, ARTWORK_NOT_FOUND);})
                .toEntity(String.class)
                .block();
        return requireNonNull(response);
    }

}
