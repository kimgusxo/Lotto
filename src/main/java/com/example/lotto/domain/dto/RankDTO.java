package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankDTO {

    private Integer ranking;
    private Integer winningCount;
    private Long totalWinningAmount;
    private Long winningAmount;

    public Rank toEntity() {
        return Rank.builder()
                .ranking(ranking)
                .winningCount(winningCount)
                .totalWinningAmount(totalWinningAmount)
                .winningAmount(winningAmount)
                .build();
    }
}
