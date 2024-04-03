package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankDTO {

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer ranking;

    @NotNull
    @Min(value = 1)
    private Integer winningCount;

    @NotNull
    @Min(value = 1)
    private Long totalWinningAmount;

    @NotNull
    @Min(value = 1)
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
