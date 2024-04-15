package com.example.lotto.repository;

import com.example.lotto.domain.StatLotto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatLottoRepository extends MongoRepository<String, StatLotto> {

}
