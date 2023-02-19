package jkarcsi.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GalleryMessages {
    public static final String RETRIEVE = "Retrieve a single artwork by its ID";
    public static final String PAGINATED = "Retrieve paginated artworks with an option to set the page number and page size";
    public static final String BUY = "Buying an artwork";
    public static final String LIST = "Listing all artworks owned by a user";

    public static final String ARTWORK_NOT_FOUND = "The provided id does not match any of the artworks";
    public static final String WEBCLIENT_FORBIDDEN = "The specified URL cannot be found or there are no rights to reach it";
    public static final String PAGES_EXCEEDED = "The specified page number (with this limit) cannot be found";
    public static final String ARTWORK_SOLD = "This artwork is not available for sale";
}
