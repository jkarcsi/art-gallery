package jkarcsi.utils.exception;

import static jkarcsi.utils.constants.UserMessages.ACCESS_DENIED;
import static jkarcsi.utils.constants.UserMessages.USER_ERROR;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest,
                        ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
            }
        };
    }

    @ExceptionHandler(CustomAuthException.class)
    public void handleCustomAuthException(HttpServletResponse res, CustomAuthException ex) throws IOException {
        res.sendError(ex.getApiStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(CustomGalleryException.class)
    public void handleCustomGalleryException(HttpServletResponse res, CustomGalleryException ex) throws IOException {
        res.sendError(ex.getApiStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), ACCESS_DENIED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(HttpServletResponse res, MethodArgumentNotValidException ex)
            throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletResponse res, IllegalArgumentException ex)
            throws IOException {
        res.sendError(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException(HttpServletResponse res, IllegalStateException ex) throws IOException {
        res.sendError(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public void handleNullPointerException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), USER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), USER_ERROR);
    }

}
