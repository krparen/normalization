package test.task.normalization.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceExceptionResponse {
    private String message;
}
