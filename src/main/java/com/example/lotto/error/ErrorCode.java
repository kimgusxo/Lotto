package com.example.lotto.error;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNKNOWN_TOKEN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    NOT_EXIST_ROUND_TOKEN("001_NOT_EXIST_ROUND", "해당 회차가 존재하지 않습니다."),
    NOT_EXIST_NUMBER_TOKEN("002_NOT_EXIST_NUMBER", "번호가 존재하지 않습니다."),
    NOT_EXIST_BONUS_NUMBER_TOKEN("003_NOT_EXIST_BONUS_NUMBER", "보너스 번호가 존재하지 않습니다."),
    NOT_EXIST_RESULT_TOKEN("004_NOT_EXIST_RESULT", "결과가 존재하지 않습니다."),
    INCORRECT_DATE_TOKEN("005_INCORRECT_DATE_RESULT", "올바른 날짜가 아닙니다."),
    DUPLICATE_ROUND_TOKEN("006_DUPLICATE_ROUND", "해당 회차는 이미 존재합니다.");

    private final String code;
    private final String detail;

}

