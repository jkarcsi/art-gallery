package jkarcsi.configuration;

import static jkarcsi.utils.constants.ExternalRequestParams.DATA;
import static jkarcsi.utils.constants.ExternalRequestParams.LIMIT;
import static jkarcsi.utils.constants.ExternalRequestParams.CURRENT_PAGE;
import static jkarcsi.utils.constants.ExternalRequestParams.PAGINATION;
import static jkarcsi.utils.constants.ExternalRequestParams.TOTAL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.ArtworkPage;

public class ArtworkPageDeserializer extends StdDeserializer<ArtworkPage> {

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
        final JsonNode jsonData = tree.get(DATA);

        if (null != jsonData && !jsonData.isNull() && jsonData.isArray()) {
            for (JsonNode artworkNode : jsonData) {
                Artwork artwork = ArtworkDeserializer.deserializeArtworkProperties(artworkNode);
                data.add(artwork);
            }
        }

        final long total = tree.get(PAGINATION).get(TOTAL).asLong();
        final int limit = tree.get(PAGINATION).get(LIMIT).asInt();
        final int currentPage = tree.get(PAGINATION).get(CURRENT_PAGE).asInt();

        return new ArtworkPage().setArtworks(data).setTotalRecords(total).setPageSize(limit).setPage(currentPage);
    }
}
