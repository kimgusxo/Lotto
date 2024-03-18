package com.example.lotto.repository;

import com.example.lotto.domain.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
    Result findByRound(Integer round);
    Result findByBonusNumber(Integer bonusNumber);
}
