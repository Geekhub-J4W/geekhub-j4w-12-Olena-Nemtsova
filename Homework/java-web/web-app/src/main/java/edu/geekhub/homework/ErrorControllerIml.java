package edu.geekhub.homework;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tinylog.Logger;

@RestControllerAdvice
public class ErrorControllerIml {

    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletRequest req, Exception ex) {
        Logger.error("Request: " + req.getRequestURL() + " raised " + ex);
    }
}
