package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.WinningReport;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class WinningReportDTO {

    private Integer round;
    private LocalDate date;
    private Long totalWinningAmount;
    private List<RankDTO> rankDTOList;

    public WinningReport toEntity() {
        List<Rank> rankList = new ArrayList<>();

        rankDTOList.forEach((r) ->
                rankList.add(r.toEntity()));

        return WinningReport.builder()
                .round(round)
                .date(date)
                .totalWinningAmount(totalWinningAmount)
                .rankList(rankList)
                .build();
    }
}
