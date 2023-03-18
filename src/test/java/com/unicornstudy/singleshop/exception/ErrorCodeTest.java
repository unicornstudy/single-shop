package com.unicornstudy.singleshop.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void 필드_테스트() {
        assertThat(ErrorCode.BAD_REQUEST_ITEMS_READ.getStatus()).isEqualTo(400);
        assertThat(ErrorCode.BAD_REQUEST_ITEMS_READ.getErrorCode()).isEqualTo("id 입력 오류");
        assertThat(ErrorCode.BAD_REQUEST_ITEMS_READ.getMessage()).isEqualTo("해당상품을 조회할 수 없습니다.");
    }
}