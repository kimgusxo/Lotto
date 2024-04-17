package com.example.lotto.repository;

import com.example.lotto.domain.StatLotto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatLottoRepository extends MongoRepository<StatLotto, String> {
    Boolean existsByNumber(Integer number);
    StatLotto findByNumber(Integer number);
    List<StatLotto> findByNumberIn(List<Integer> numberList);
}
