package com.unicornstudy.singleshop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST_ITEMS_READ(400, "id 입력 오류", "해당상품을 조회할 수 없습니다."),
    BAD_REQUEST_ITEMS_UPDATE(400, "id 입력 오류", "해당상품을 수정할 수 없습니다."),
    BAD_REQUEST_ITEMS_DELETE(400, "id 입력 오류", "해당상품을 삭제할 수 없습니다."),

    ITEMS_NOT_FOUND(404, "NOT_FOUND", "해당 상품의 정보를 찾을 수 없습니다.");

    private final int status;
    private final String errorCode;
    private final String message;

}
