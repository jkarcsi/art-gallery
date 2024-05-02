package jkarcsi.configuration;

import static jkarcsi.utils.constants.ExternalRequestParams.ALT_TEXT;
import static jkarcsi.utils.constants.ExternalRequestParams.AUTHOR;
import static jkarcsi.utils.constants.ExternalRequestParams.DATA;
import static jkarcsi.utils.constants.ExternalRequestParams.HEIGHT;
import static jkarcsi.utils.constants.ExternalRequestParams.IMAGE_ID;
import static jkarcsi.utils.constants.ExternalRequestParams.LQIP;
import static jkarcsi.utils.constants.ExternalRequestParams.THUMBNAIL;
import static jkarcsi.utils.constants.ExternalRequestParams.TITLE;
import static jkarcsi.utils.constants.ExternalRequestParams.WIDTH;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.Thumbnail;

public class ArtworkDeserializer extends StdDeserializer<Artwork> {

    public ArtworkDeserializer() {
        this(null);
    }

    public ArtworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Artwork deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode tree = jp.getCodec().readTree(jp);
        JsonNode jsonNode = tree.get(DATA);
        return deserializeArtworkProperties(jsonNode);
    }

    protected static Artwork deserializeArtworkProperties(JsonNode jsonNode) {
        final Artwork artwork = new Artwork();
        artwork.setId(jsonNode.get(IMAGE_ID).intValue());
        artwork.setTitle(jsonNode.get(TITLE).textValue());
        artwork.setAuthor(jsonNode.get(AUTHOR).textValue());
        if (!jsonNode.get(THUMBNAIL).isNull()) {
            artwork.setThumbnail(new Thumbnail(jsonNode.get(THUMBNAIL).get(LQIP).asText(),
                    jsonNode.get(THUMBNAIL).get(WIDTH).asInt(),
                    jsonNode.get(THUMBNAIL).get(HEIGHT).asInt(),
                    jsonNode.get(THUMBNAIL).get(ALT_TEXT).asText()));
        }
        return artwork;
    }

}
