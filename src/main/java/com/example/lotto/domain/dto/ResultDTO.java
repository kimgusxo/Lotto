package com.example.lotto.domain.dto;

import com.example.lotto.domain.Result;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ResultDTO {

    @NotNull
    @Min(value = 1)
    private Integer round;

    @Size(min = 6, max = 6)
    private List<@Min(value = 1) @Max(value = 45) Integer> numbers;

    @NotNull
    @Min(value = 1)
    @Max(value = 45)
    private Integer bonusNumber;

    @Past
    private LocalDate date;

    public Result toEntity() {
        return Result.builder()
                .round(round)
                .numbers(numbers)
                .bonusNumber(bonusNumber)
                .date(date)
                .build();
    }

}
