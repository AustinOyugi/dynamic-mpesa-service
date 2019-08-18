package ke.paystep.mpesaservicefull.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Austin Oyugi on 17/8/2019.
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}