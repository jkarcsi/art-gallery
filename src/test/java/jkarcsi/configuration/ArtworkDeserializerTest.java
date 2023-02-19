package jkarcsi.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.Thumbnail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@TestWithResources
class ArtworkDeserializerTest {

    private ObjectMapper mapper;

    private Artwork artwork;

    @GivenTextResource("txt/ArtworkRawString.txt")
    String artworkString;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        artwork = loadArtworkData();
    }

    @Test
    void test_deserialize_successful() throws IOException {

        Artwork result = mapper.readValue(artworkString, Artwork.class);

        assertThat(result).isNotNull().hasNoNullFieldsOrProperties().isEqualTo(artwork);
    }

    @Test
    void test_deserializePartially_fail() throws IOException {

        Artwork result = mapper.readValue(artworkString, Artwork.class);

        artwork.setId(null);

        assertThat(result).isNotEqualTo(artwork);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void test_deserializeNullAndEmpty_fail(String nullOrEmpty) throws IllegalArgumentException {
        assertThatThrownBy(() -> mapper.readValue(nullOrEmpty, Artwork.class)).isInstanceOfAny(IllegalArgumentException.class, MismatchedInputException.class);
    }

    private Artwork loadArtworkData() {
        return new Artwork().setId(129884).setTitle("Starry Night and the Astronauts").setAuthor("Alma Thomas")
                .setThumbnail(new Thumbnail(
                        "data:image/gif;base64,R0lGODlhBAAFAPQAABw/Zhg/aBRBaBZBahRCaxxBahxEahNIchZJcR9LdB9OdiZIZSBEbShLbjxRZyBPeipRcSpReUpWaitXgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAURoMJIDhJAywAcAlEkxhNNTQgAOw==",
                        5376, 6112,
                        "Abstract painting composed of small vertical dabs of multiple shades of blue with a small area of similar strokes of red, orange, and yellow in the upper right."));
    }

}