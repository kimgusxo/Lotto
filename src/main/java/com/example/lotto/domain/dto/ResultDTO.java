package com.example.lotto.domain.dto;

import com.example.lotto.domain.Result;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ResultDTO {

    @Min(value = 1)
    private Integer round;

    @Min(value = 1)
    @Max(value = 45)
    private List<Integer> numbers;

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
