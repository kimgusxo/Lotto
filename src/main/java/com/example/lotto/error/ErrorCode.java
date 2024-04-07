package com.example.lotto.error;

import lombok.RequiredArgsConstructor;
import lombok.Getter;


/**
 * 000번대: UNKNOWN
 * 100번대: RESULT
 * 200번대: WINNING_REPORT
 * 300번대: CRAWLING_MODEL
 * 400번대: VALIDATION
 **/
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 000번대
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),

    // 100번대
    NOT_EXIST_RESULT("100_NOT_EXIST_RESULT", "결과가 존재하지 않습니다."),
    NOT_EXIST_RESULT_ROUND("101_NOT_RESULT_ROUND", "해당 회차가 존재하지 않습니다."),
    NOT_EXIST_RESULT_NUMBER("102_NOT_EXIST_RESULT_ROUND", "번호가 존재하지 않습니다."),
    NOT_EXIST_RESULT_BONUS_NUMBER("103_NOT_EXIST_RESULT_BONUS_NUMBER", "보너스 번호가 존재하지 않습니다."),
    DUPLICATE_RESULT_ROUND("104_DUPLICATE_ROUND", "해당 회차는 이미 존재합니다."),
    INCORRECT_RESULT_DATE("105_INCORRECT_DATE_RESULT", "올바른 날짜가 아닙니다."),

    // 200번대
    NOT_EXIST_WINNING_REPORT("200_NOT_EXIST_WINNING_REPORT", "내용이 존재하지 않습니다."),
    NOT_EXIST_WINNING_REPORT_ROUND("201_NOT_EXIST_WINNING_REPORT_ROUND", "해당 회차가 존재하지 않습니다."),
    DUPLICATE_WINNING_REPORT_ROUND("202_DUPLICATE_WINNING_REPORT_ROUND_TOKEN", "해당 회차는 이미 존재합니다."),
    INCORRECT_WINNING_REPORT_DATE("202_INCORRECT_WINNING_REPORT_DATE_TOKEN", "올바른 날짜가 아닙니다."),

    // 300번대
    NOT_EXIST_CRAWLING_MODEL("300_NOT_EXIST_CRAWLING_MODEL", "크롤링 데이터가 존재하지 않습니다."),

    // 400번대
    VALIDATION("400_VALIDATION", "형식이 일치하지 않습니다.");

    private final String code;
    private final String detail;

}

