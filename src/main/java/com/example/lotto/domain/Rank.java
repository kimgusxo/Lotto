package com.example.lotto.domain;

import com.example.lotto.domain.dto.RankDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rank")
public class Rank {

    @Id
    private String id;

    private Integer ranking;
    private Integer winningCount;
    private Long totalWinningAmount;
    private Long winningAmount;

    public RankDTO toDTO() {
        return RankDTO.builder()
                .ranking(ranking)
                .winningCount(winningCount)
                .totalWinningAmount(totalWinningAmount)
                .winningAmount(winningAmount)
                .build();
    }

}
