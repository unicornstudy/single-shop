package com.unicornstudy.singleshop.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void 생성자_테스트() {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST_ITEMS_READ;
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        assertThat(errorResponse.getStatus()).isEqualTo(errorCode.getStatus());
        assertThat(errorResponse.getMessage()).isEqualTo(errorCode.getMessage());
        assertThat(errorResponse.getCode()).isEqualTo(errorCode.getErrorCode());
    }

    @Test
    void 수정자_테스트() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ITEMS_NOT_FOUND);

        errorResponse.setStatus(500);
        errorResponse.setMessage("서버 오류");
        errorResponse.setCode("SERVER_ERROR");

        assertThat(errorResponse.getStatus()).isEqualTo(500);
        assertThat(errorResponse.getMessage()).isEqualTo("서버 오류");
        assertThat(errorResponse.getCode()).isEqualTo("SERVER_ERROR");
    }
}