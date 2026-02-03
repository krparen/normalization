package test.task.normalization.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.task.normalization.exception.ServiceException;
import test.task.normalization.exception.ServiceExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<ServiceExceptionResponse> handleServiceException(ServiceException ex) {
        ServiceExceptionResponse responseBody = new ServiceExceptionResponse(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(responseBody);
    }
}
