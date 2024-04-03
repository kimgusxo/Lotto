package com.example.lotto.domain.dto;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.WinningReport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class WinningReportDTO {

    @NotNull
    @Min(value = 1)
    private Integer round;

    @NotNull
    @Past
    private LocalDate date;

    @NotNull
    @Min(value = 1)
    private Long totalWinningAmount;

    @NotEmpty
    @Valid
    @Size(min = 5, max = 5)
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
