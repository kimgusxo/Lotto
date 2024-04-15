package com.example.lotto.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDTO {

    private String code;
    private String detail;

    public static ErrorDTO of(ErrorCode errorCode) {
        return ErrorDTO.builder()
                .code(errorCode.getCode())
                .detail(errorCode.getDetail())
                .build();
    }

}
