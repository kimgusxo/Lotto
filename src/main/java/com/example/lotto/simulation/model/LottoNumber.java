package com.example.lotto.simulation.model;

import lombok.Data;

import java.util.List;

@Data
public class LottoNumber {

    private List<Integer> numbers;
    private Integer bonusNumber;

}
