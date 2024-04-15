package com.example.lotto.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatLottoDTO {

    private Integer number;
    private Integer count;
    private Double probability;
    private Integer bonusCount;
    private Double bonusProbability;

}
