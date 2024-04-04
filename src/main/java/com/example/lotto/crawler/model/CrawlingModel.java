package com.example.lotto.crawler.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CrawlingModel {

    private Integer round;
    private LocalDate date;
    private List<Integer> numbers;
    private Integer bonusNumber;
    private Long totalWinningAmount;
    private List<Integer> rankings;
    private List<Integer> winningCounts;
    private List<Long> totalWinningAmounts;
    private List<Long> winningAmounts;

}
