package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.StatLotto;
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

    public StatLotto toEntity() {
        return StatLotto.builder()
                .number(number)
                .count(count)
                .probability(probability)
                .bonusCount(bonusCount)
                .bonusProbability(bonusProbability)
                .build();
    }

}
