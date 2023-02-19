package jkarcsi.dto.gallery;

import static jkarcsi.utils.constants.ExternalRequestParams.AUTHOR;
import static jkarcsi.utils.constants.ExternalRequestParams.IMAGE_ID;
import static jkarcsi.utils.constants.ExternalRequestParams.THUMBNAIL;
import static jkarcsi.utils.constants.ExternalRequestParams.TITLE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jkarcsi.configuration.ArtworkDeserializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ArtworkDeserializer.class)
public class Artwork {
    @JsonProperty(IMAGE_ID)
    private Integer id;
    @JsonProperty(TITLE)
    private String title;
    @JsonProperty(AUTHOR)
    private String author;
    @JsonProperty(THUMBNAIL)
    private Thumbnail thumbnail;

}
