package com.unicornstudy.singleshop.orders.exception;

public class ItemQuantityException extends OrderException {

    public static final String ERROR_MESSAGE = ": 상품 재고가 부족합니다.";
    private String itemName;

    public ItemQuantityException(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getMessage() {
        return itemName + ERROR_MESSAGE;
    }
}
