package jkarcsi.utils.constants;

import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExternalRequestParams {

    //Artwork
    public static final String IMAGE_ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "artist_title";
    public static final String THUMBNAIL = "thumbnail";
    public static final List<String> ARTIC_PARAMS = List.of(IMAGE_ID, TITLE, AUTHOR, THUMBNAIL);

    //ArtworkPage
    public static final String ARTWORKS = "data";
    public static final String TOTAL_RECORDS = "total";
    public static final String PAGE_SIZE = "limit";
    public static final String PAGE = "current_page";

    // Thumbnail
    public static final String LQIP = "lqip";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String ALT_TEXT = "alt_text";
}
