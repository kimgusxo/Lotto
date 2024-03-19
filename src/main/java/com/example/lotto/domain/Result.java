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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "result")
public class Result {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer round;
    private List<Integer> numbers;
    private Integer bonusNumber;
    private LocalDate date;

}
