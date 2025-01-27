package com.julioneves.requisicao.web.exception;

import com.julioneves.requisicao.exception.ClienteNotFoundException;
import com.julioneves.requisicao.exception.DuplicateClienteException;
import com.julioneves.requisicao.exception.InvalidClienteDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidClienteDataException.class)
    public ResponseEntity<ErrorMessage> handleInvalidClienteDataException(InvalidClienteDataException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }


    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleClienteNotFoundException(ClienteNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }
    @ExceptionHandler(DuplicateClienteException.class)
    public ResponseEntity<String> handleDuplicateClienteException(DuplicateClienteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Ocorreu um erro inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

