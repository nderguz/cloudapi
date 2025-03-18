package diploma.cloudapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> commonExceptionHandler(Exception ex){
        log.error("Caught some exception", ex);
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message){
        var errorResponseBody = ErrorResponse.builder()
                .message(message).build();

        return ResponseEntity.status(status).body(errorResponseBody);
    }
}
