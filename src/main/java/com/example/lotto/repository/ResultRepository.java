package com.example.lotto.repository;

import com.example.lotto.domain.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {

    Result findByRound(Integer round);
    Result findByBonusNumber(Integer bonusNumber);
    List<Result> findByNumbersContaining(Integer number);
    List<Result> findByDateBetween(LocalDate startDate, LocalDate endDate);
    boolean deleteByRound(Integer round);

}
