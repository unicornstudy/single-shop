package com.unicornstudy.singleshop.exception;

import com.unicornstudy.singleshop.exception.carts.CartException;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.exception.payments.SubscriptionApproveException;
import com.unicornstudy.singleshop.exception.util.BodyCreator;
import com.unicornstudy.singleshop.orders.command.application.OrderService;
import com.unicornstudy.singleshop.exception.orders.OrderException;
import com.unicornstudy.singleshop.exception.payments.ApproveException;
import com.unicornstudy.singleshop.exception.payments.PaymentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final OrderService orderService;

    @ExceptionHandler(ItemsException.class)
    public ResponseEntity<ErrorResponse> handleItemsException(ItemsException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<Map<String, String>> handleCartException(CartException e) {
        return new ResponseEntity<>(BodyCreator.createErrorBody(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> handleOrderException(OrderException e) {
        return new ResponseEntity<>(BodyCreator.createErrorBody(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentsException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(PaymentsException e) {
        return new ResponseEntity<>(BodyCreator.createErrorBody(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApproveException.class)
    public ResponseEntity<Map<String, String>> handleApproveException(ApproveException e) {
        orderService.handleOrderPaymentError(e.getOrderId());
        return new ResponseEntity<>(BodyCreator.createErrorBody(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SubscriptionApproveException.class)
    public ResponseEntity<Map<String, String>> handleSubscriptionApproveException(SubscriptionApproveException e) {
        return new ResponseEntity<>(BodyCreator.createErrorBody(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return new ResponseEntity<>(BodyCreator.createErrorBody(errorMessage), HttpStatus.BAD_REQUEST);
    }
}
