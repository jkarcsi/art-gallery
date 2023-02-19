package jkarcsi.configuration;

import static jkarcsi.utils.constants.ExternalRequestParams.ALT_TEXT;
import static jkarcsi.utils.constants.ExternalRequestParams.AUTHOR;
import static jkarcsi.utils.constants.ExternalRequestParams.HEIGHT;
import static jkarcsi.utils.constants.ExternalRequestParams.IMAGE_ID;
import static jkarcsi.utils.constants.ExternalRequestParams.LQIP;
import static jkarcsi.utils.constants.ExternalRequestParams.THUMBNAIL;
import static jkarcsi.utils.constants.ExternalRequestParams.TITLE;
import static jkarcsi.utils.constants.ExternalRequestParams.WIDTH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.ArtworkPage;
import jkarcsi.dto.gallery.Thumbnail;

public class ArtworkPageDeserializer extends StdDeserializer<ArtworkPage> {

    public static final String PAGINATION = "pagination";

    public ArtworkPageDeserializer() {
        this(null);
    }

    public ArtworkPageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ArtworkPage deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode tree = jp.getCodec().readTree(jp);

        final List<Artwork> data = new ArrayList<>();
        final JsonNode jsonData = tree.get("data");

        if (jsonData.isArray()) {
            for (JsonNode artworkNode : jsonData) {
                Artwork artwork = new Artwork();
                artwork.setId(artworkNode.get(IMAGE_ID).intValue());
                artwork.setTitle(artworkNode.get(TITLE).textValue());
                artwork.setAuthor(artworkNode.get(AUTHOR).textValue());
                artwork.setThumbnail(new Thumbnail(artworkNode.get(THUMBNAIL).get(LQIP).asText(),
                        artworkNode.get(THUMBNAIL).get(WIDTH).asInt(), artworkNode.get(THUMBNAIL).get(HEIGHT).asInt(),
                        artworkNode.get(THUMBNAIL).get(ALT_TEXT).asText()));
                data.add(artwork);
            }
        }

        final long total = tree.get(PAGINATION).get("total").asLong();
        final int limit = tree.get(PAGINATION).get("limit").asInt();
        final int currentPage = tree.get(PAGINATION).get("current_page").asInt();

        return new ArtworkPage().setArtworks(data).setTotalRecords(total).setPageSize(limit).setPage(currentPage);
    }
}
