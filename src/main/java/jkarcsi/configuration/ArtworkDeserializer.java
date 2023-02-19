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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.Thumbnail;

public class ArtworkDeserializer extends StdDeserializer<Artwork> {

    public static final String DATA = "data";

    public ArtworkDeserializer() {
        this(null);
    }

    public ArtworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Artwork deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode productNode = jp.getCodec().readTree(jp);
        final Artwork artwork = new Artwork();

        artwork.setId(productNode.get(DATA).get(IMAGE_ID).intValue());
        artwork.setTitle(productNode.get(DATA).get(TITLE).textValue());
        artwork.setAuthor(productNode.get(DATA).get(AUTHOR).textValue());
        artwork.setThumbnail(new Thumbnail(productNode.get(DATA).get(THUMBNAIL).get(LQIP).asText(),
                productNode.get(DATA).get(THUMBNAIL).get(WIDTH).asInt(),
                productNode.get(DATA).get(THUMBNAIL).get(HEIGHT).asInt(),
                productNode.get(DATA).get(THUMBNAIL).get(ALT_TEXT).asText()));

        return artwork;
    }

}
