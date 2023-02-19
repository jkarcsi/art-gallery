package jkarcsi.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CustomGalleryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus apiStatus;
    private final String message;

}
