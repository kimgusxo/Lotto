package com.example.lotto.domain;

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
@Document(collection = "stat_lotto")
public class StatLotto {

    @Id
    private String id;

    @Indexed(unique = true)
    private Integer number;
    private Integer count;
    private Double probability;
    private Integer bonusCount;
    private Double bonusProbability;

}
