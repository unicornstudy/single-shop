package com.unicornstudy.singleshop.exception;

import com.unicornstudy.singleshop.carts.exception.NotFoundException;
import com.unicornstudy.singleshop.items.exception.ErrorResponse;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemsException.class)
    public ResponseEntity<ErrorResponse> handleItemsException(ItemsException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handle(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
