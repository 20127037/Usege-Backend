package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * exception
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:20 PM
 * Description: ...
 */

public class NoSuchElementFoundException extends ResponseStatusException {

    public NoSuchElementFoundException(String elementId, String collection) {
        super(HttpStatus.NOT_FOUND, String.format("%s was not found in %s", elementId, collection));
    }
    public NoSuchElementFoundException(String elementId) {
        super(HttpStatus.NOT_FOUND, String.format("%s was not found", elementId));
    }
}
