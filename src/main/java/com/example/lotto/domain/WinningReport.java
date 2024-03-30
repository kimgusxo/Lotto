package com.example.lotto.domain;

import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public WinningReportDTO toDTO() {
        List<RankDTO> rankDTOList = new ArrayList<>();

        rankList.forEach((r) ->
                rankDTOList.add(r.toDTO()));

        return WinningReportDTO.builder()
                .round(round)
                .date(date)
                .totalWinningAmount(totalWinningAmount)
                .rankDTOList(rankDTOList)
                .build();
    }

}
