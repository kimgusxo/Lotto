package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer ranking;

    @NotNull
    private Integer winningCount;

    @NotNull
    private Long totalWinningAmount;

    @NotNull
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
