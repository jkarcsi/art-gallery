package jkarcsi.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GeneralConstants {

    // Requests
    public static final String ARTIC_API_HOST = "https://api.artic.edu/api/v1/artworks";
    public static final String USERS_BASE_PATH = "/users";
    public static final String GALLERY_BASE_PATH = "/artworks";
    public static final String ID_PATH = "/{id}";
    public static final String SIGNIN_PATH = "/signin";
    public static final String SIGNUP_PATH = "/signup";
    public static final String USERNAME_PATH = "/{username}";
    public static final String ME_PATH = "/me";
    public static final String REFRESH_PATH = "/refresh";

    // Authorization
    public static final String ACCESS_BOTH = "hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')";
    public static final String ACCESS_CLIENT = "hasRole('ROLE_CLIENT')";
    public static final String ACCESS_ADMIN = "hasRole('ROLE_ADMIN')";

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
