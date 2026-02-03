package test.task.normalization.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    @Getter
    @Setter
    private HttpStatus httpStatus;

    public ServiceException(String message) {
        super(message);
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
