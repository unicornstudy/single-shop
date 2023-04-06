package com.unicornstudy.singleshop.exception;

import com.unicornstudy.singleshop.exception.carts.CartException;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.exception.payments.SubscriptionApproveException;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.exception.orders.OrderException;
import com.unicornstudy.singleshop.exception.payments.ApproveException;
import com.unicornstudy.singleshop.exception.payments.PaymentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<String> handleCartException(CartException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrderException(OrderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PaymentsException.class)
    public ResponseEntity<String> handlePaymentException(PaymentsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ApproveException.class)
    public ResponseEntity<String> handleApproveException(ApproveException e) {
        orderService.handleOrderPaymentError(e.getOrderId());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SubscriptionApproveException.class)
    public ResponseEntity<String> handleSubscriptionApproveException(SubscriptionApproveException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
