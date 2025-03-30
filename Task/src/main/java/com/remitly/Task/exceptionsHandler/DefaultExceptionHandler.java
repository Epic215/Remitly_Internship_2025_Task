package com.remitly.Task.exceptionsHandler;

import com.remitly.Task.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception e) {
        return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {

        return new ResponseEntity<>(new ResponseDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder messages = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(
                error ->{
                    String errorField = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    String message = errorField + ": " + errorMessage + ", ";
                    messages.append(message);
                });

        return new ResponseEntity<>(new ResponseDTO(messages.toString()), HttpStatus.BAD_REQUEST);
    }

}
