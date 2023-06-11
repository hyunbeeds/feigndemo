package com.polarbear.feigndemo.config.error;

import com.polarbear.feigndemo.config.error.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<ErrorResponse> handleClientException(Exception e) {
        ErrorResponse.Error error = new ErrorResponse.Error(e.getClass().getTypeName(), e.getMessage());
        if (e instanceof ClientException) {
            return new ResponseEntity<>(new ErrorResponse(error), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponse(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
