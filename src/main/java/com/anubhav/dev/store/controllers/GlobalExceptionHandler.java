package com.anubhav.dev.store.controllers;

import com.anubhav.dev.store.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleUnreadableMessages() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorDto("Invalid Request Body")
    );
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    Map<String, String> errors = new HashMap<>();
    methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach((fieldError) -> {
      String field = fieldError.getField();
      String message = fieldError.getDefaultMessage();
      errors.put(field, message);
    });
    return ResponseEntity.badRequest().body(errors);
  }
}
