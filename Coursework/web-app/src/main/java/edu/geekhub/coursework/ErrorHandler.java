package edu.geekhub.coursework;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tinylog.Logger;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleError(
        HttpServletRequest request,
        AccessDeniedException ex
    ) {
        Logger.warn("Request: " + request.getRequestURL() + " raised " + ex);

        return new ResponseEntity<>(
            ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN
        );
    }

    @MessageExceptionHandler({AccessDeniedException.class})
    public <T> void handleException(Message<T> message, AccessDeniedException ex) {
        Logger.warn("Message:" + message.getHeaders().get("lookupDestination") + " raised " + ex);
    }
}
