package jkarcsi.utils.constants;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class GeneralConstants {

    // Host
    @Value("${paths.host}")
    public String host;

    // Paths
    // Users
    public static final String USERS_BASE = "${paths.users.base}";
    public static final String USERS_POST_SIGNIN = "${paths.users.post.login}";
    public static final String USERS_POST_SIGNUP = "${paths.users.post.register}";
    public static final String USERS_DELETE_REMOVE = "${paths.users.delete.remove}";
    public static final String USERS_GET_OWNERSHIP = "${paths.users.get.ownership}";
    public static final String USERS_GET_SELF = "${paths.users.get.self}";
    public static final String USERS_GET_ALL_USERS = "${paths.users.get.users}";
    public static final String USERS_GET_REFRESH = "${paths.users.get.refresh}";

    // Paths
    // Gallery
    public static final String GALLERY_BASE = "${paths.gallery.base}";
    public static final String GALLERY_GET_SINGLE = "${paths.gallery.get.single}";
    public static final String GALLERY_POST_PURCHASE = "${paths.gallery.post.purchase}";


    // Swagger
    public static final String AUTHORIZATION = "Authorization";

    // Logging
    public static final String REPLACEMENT = "XXXXXXXXX";
    public static final String WEBCLIENT_SUCCESS = "External request: {}";
    public static final String WEBCLIENT_FAIL = "External request URL wrong: {}";

    // Pagination response headers
    public static final String PAGINATION_COUNT = "Pagination-Count";
    public static final String PAGINATION_PAGE = "Pagination-Page";
    public static final String PAGINATION_LIMIT = "Pagination-Limit";

}
