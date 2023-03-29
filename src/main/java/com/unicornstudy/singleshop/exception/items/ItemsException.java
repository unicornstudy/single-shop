package com.unicornstudy.singleshop.exception.items;

import com.unicornstudy.singleshop.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ItemsException extends RuntimeException {

    private ErrorCode errorCode;

    public ItemsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
