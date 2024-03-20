package com.example.lotto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rank")
public class Rank {

    @Id
    private String id;

    private Integer rank;

    private Integer winningCount;
    private Long totalWinningAmount;
    private Long winningAmount;

}
