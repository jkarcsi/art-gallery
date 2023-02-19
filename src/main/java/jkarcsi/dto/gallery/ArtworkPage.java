package jkarcsi.dto.gallery;

import static jkarcsi.utils.constants.ExternalRequestParams.ARTWORKS;
import static jkarcsi.utils.constants.ExternalRequestParams.PAGE;
import static jkarcsi.utils.constants.ExternalRequestParams.PAGE_SIZE;
import static jkarcsi.utils.constants.ExternalRequestParams.TOTAL_RECORDS;

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
    @JsonProperty(ARTWORKS)
    private List<Artwork> artworks;
    @JsonProperty(TOTAL_RECORDS)
    private Long totalRecords;
    @JsonProperty(PAGE_SIZE)
    private Integer pageSize;
    @JsonProperty(PAGE)
    private Integer page;
}
