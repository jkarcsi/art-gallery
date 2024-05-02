package jkarcsi.dto.gallery;

import static jkarcsi.utils.constants.ExternalRequestParams.CURRENT_PAGE;
import static jkarcsi.utils.constants.ExternalRequestParams.DATA;
import static jkarcsi.utils.constants.ExternalRequestParams.LIMIT;
import static jkarcsi.utils.constants.ExternalRequestParams.TOTAL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jkarcsi.configuration.ArtworkPageDeserializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ArtworkPageDeserializer.class)
public class ArtworkPage {
    @JsonProperty(DATA)
    private List<Artwork> artworks;
    @JsonProperty(TOTAL)
    private Long totalRecords;
    @JsonProperty(LIMIT)
    private Integer pageSize;
    @JsonProperty(CURRENT_PAGE)
    private Integer page;
}
