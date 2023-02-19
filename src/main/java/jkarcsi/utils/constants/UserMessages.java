package jkarcsi.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMessages {

    // Endpoints
    public static final String SIGN_IN = "Authenticates user and returns its JWT token";
    public static final String SIGN_UP = "Creates user and returns its JWT token";
    public static final String ME = "Returns current user's data";
    public static final String DELETE = "Deletes specific user by username";

    // Errors
    public static final String TOKEN_ERROR = "Expired or invalid JWT token";
    public static final String INVALID_DATA = "Invalid username/password supplied";
    public static final String USER_ERROR = "Something went wrong";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String NOT_EXIST = "The user doesn't exist";
    public static final String ALREADY_EXIST = "Username is already in use";
}
