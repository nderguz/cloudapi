package diploma.cloudapi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorAdvice> commonExceptionHandler(Exception ex){
        log.error("Caught some exception", ex);
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorAdvice> response(HttpStatus status, String message){
        var errorResponseBody = new ErrorAdvice(message, status.value());
        return ResponseEntity.status(status).body(errorResponseBody);
    }

    @Data
    @AllArgsConstructor
    private class ErrorAdvice{
        private String message;
        private Integer id;
    }
}
