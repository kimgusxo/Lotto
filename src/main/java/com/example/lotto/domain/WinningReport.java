package com.example.lotto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "winning_report")
public class WinningReport {

    @Id
    private String id;
    @Indexed(unique = true)
    private Integer round;
    private LocalDate date;

    private Long totalWinningAmount;

    private List<Rank> rankList;

}
