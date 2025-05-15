package fer.infsus.Odbojka.rest;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Order(Integer.MIN_VALUE)
@ControllerAdvice
public class RestExceptionHandler {
    public RestExceptionHandler() {
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<?> handleIllegalArgument(Exception e) {
        Map<String, String> props = new HashMap();
        props.put("message", e.getMessage());
        props.put("status", "400");
        props.put("error", "Bad Request");
        return new ResponseEntity(props, HttpStatus.BAD_REQUEST);
    }
}
