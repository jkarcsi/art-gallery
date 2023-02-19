package jkarcsi.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.List;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.ArtworkPage;
import jkarcsi.dto.gallery.Thumbnail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@TestWithResources
class ArtworkPageDeserializerTest {

    private ObjectMapper mapper;

    private Artwork artwork1;
    private Artwork artwork2;
    private Artwork artwork3;
    private Artwork artwork4;
    private Artwork artwork5;

    @GivenTextResource("txt/ArtworkPageRawString.txt")
    String artworkPageString;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        initArtworks();
    }

    @Test
    void test_deserialize_successful() throws IOException {

        ArtworkPage result = mapper.readValue(artworkPageString, ArtworkPage.class);

        final ArtworkPage expected =
                new ArtworkPage().setArtworks(List.of(artwork1, artwork2, artwork3, artwork4, artwork5))
                        .setTotalRecords(119049L).setPageSize(5).setPage(1);

        assertThat(result).isNotNull().hasNoNullFieldsOrProperties().isEqualTo(expected);

    }

    @Test
    void test_deserializePartialObject_fail() throws IOException {

        ArtworkPage result = mapper.readValue(artworkPageString, ArtworkPage.class);

        final ArtworkPage expected =
                new ArtworkPage().setArtworks(List.of(artwork1, artwork2, artwork3, artwork4, artwork5)).setPageSize(5)
                        .setPage(1);

        assertThat(result).isNotNull().isNotEqualTo(expected);

    }

    @ParameterizedTest
    @NullAndEmptySource
    void test_deserializeNullAndEmpty_fail(String nullOrEmpty) throws IllegalArgumentException {
        assertThatThrownBy(() -> mapper.readValue(nullOrEmpty, ArtworkPage.class)).isInstanceOfAny(
                IllegalArgumentException.class, MismatchedInputException.class);
    }

    private void initArtworks() {
        artwork1 = new Artwork().setId(11434).setTitle("Salome with the Head of Saint John the Baptist")
                .setAuthor("Guido Reni").setThumbnail(new Thumbnail(
                        "data:image/gif;base64,R0lGODlhBAAFAPQAABwXDCsjEiwjFyslFy4nGy0mHSspGS8pHDMjFDIoFzQsGkM5J006I0c1LEc5K1ZHL0xDMGBOMWdIOHpcQQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAURYDAcBRNBQjM9CiI5BpAsRAgAOw==",
                        1581, 2250,
                        "Large painting of woman, in gold and pink dress, placing severed head on platter."));

        artwork2 = new Artwork().setId(8961).setTitle("Head of Arthur Jerome Eddy").setAuthor("Auguste Rodin")
                .setThumbnail(new Thumbnail(
                        "data:image/gif;base64,R0lGODlhBQAFAPQAAD8/O0REQUpKR1FRT35+fZmZl5uamZubmZ+fn6Kioqampaenp6ioqLW1tLW1tri4uLq6ur6+vr+/v9LS0tPT0wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAFAAUAAAUVIIMQyQIhgfJMjtBM0gAMEWUUxxQCADs=",
                        2304, 2250, "A work made of bronze."));

        artwork3 = new Artwork().setId(14556).setTitle("Auvers, Panoramic View").setAuthor("Paul Cezanne").setThumbnail(
                new Thumbnail(
                        "data:image/gif;base64,R0lGODlhBgAFAPQAAGNpVmpvWGhsW2pyW2h2WW97XnBzWWtyYGt0Ym92Y255ZXR2YHV1Y3Z2Y3RyZnF5YHV4YXR5ZnV7Z3d9Z3V5cH2AZoWNX4eNX4mRi4qSjIyTjomWkZCWkpGXkgAAAAAAACH5BAAAAAAALAAAAAAGAAUAAAUYINdpWLZNipQciOEwQQMMCxRRwkMU1lWFADs=",
                        12293, 9759,
                        "View of a village surrounded by hills, fields, and trees, shown from a vantage point above the scene. Subtly rolling hills recede into a bluish gray-ish sky punctuated by white clouds. Thick brushstrokes built up atop each other are visible throughout."));

        artwork4 =
                new Artwork().setId(16564).setTitle("Branch of the Seine near Giverny (Mist)").setAuthor("Claude Monet")
                        .setThumbnail(new Thumbnail(
                                "data:image/gif;base64,R0lGODlhBQAFAPQAAG5xiHJ2iXJ2jXl6k4CCmYWImZCSqpSWr5ubr46OsZuds5qata6nrqamt6ymsaimt6emubausbOstbuzuLiyvKutxK+wxbGww6+00AAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAFAAUAAAUVICAUERMQiPQMB2ZVyXJRkKE0kxMCADs=",
                                5253, 5097,
                                "Painting of softly rendered shapes in pale blue, green, and white. A textured green mass at left resembles foliage. Blue and white cloud-like forms fill the rest of the frame."));

        artwork5 = new Artwork().setId(16487).setTitle("The Bay of Marseille, Seen from L'Estaque")
                .setAuthor("Paul Cezanne").setThumbnail(new Thumbnail(
                        "data:image/gif;base64,R0lGODlhBgAFAPQAAHx2X15rbk1le1dsfV1wdm9xaWpybnd1YX58ZmRscWJwdIJ1XoJ5Yox/ZZSJb5mIa1NqgVpxiXWDiXeDiHqGjX+Mk4OSmoWTmIqeoo2fpIyeppekoJSlqJmnqAAAAAAAACH5BAAAAAAALAAAAAAGAAUAAAUYINdlGrZdVkVJk6EMAhQ1QJEEhPMcC4OEADs=",
                        12821, 10275, "Impressionist sea landscape, tan houses, blue ocean, distant mountains."));

    }

}