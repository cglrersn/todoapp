package com.caglar.todoapp.exception;

import com.caglar.todoapp.exception.model.TodoItemBadRequestException;
import com.caglar.todoapp.exception.model.TodoItemNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, List<String>>> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(getErrorsMap(List.of("Email or password is wrong")), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, List<String>>> handleSignatureException(SignatureException ex) {
        return new ResponseEntity<>(getErrorsMap(List.of("JWT signature is invalid")), new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, List<String>>> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(getErrorsMap(List.of("JWT is expired")), new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TodoItemBadRequestException.class)
    public ResponseEntity<Map<String, List<String>>> handleTodoItemBadRequestException(
            TodoItemBadRequestException ex) {
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage())), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TodoItemNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleTodoItemBadRequestException(TodoItemNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage())), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<String>>> handleUnknownException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(getErrorsMap(List.of("Something went wrong, please try again later.")),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
