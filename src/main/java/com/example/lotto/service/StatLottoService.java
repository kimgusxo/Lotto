package com.example.lotto.service;

import com.example.lotto.domain.StatLotto;
import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.StatLottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatLottoService {

    private final StatLottoRepository statLottoRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public StatLottoService(StatLottoRepository statLottoRepository,
                            MongoTemplate mongoTemplate) {
        this.statLottoRepository = statLottoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    public List<StatLottoDTO> calcStatLotto() {
            // 집계 파이프라인 정의
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.unwind("numbers"),  // numbers 필드를 풀어헤침
                    Aggregation.group("numbers")    // numbers로 그룹화
                            .count().as("count")        // 각 숫자의 등장 횟수 계산
                            .sum("bonus").as("bonusCount"), // 보너스 번호의 출현 횟수 계산
                    Aggregation.project("count", "bonusCount") // 결과 필드 정의
                            .and("number").previousOperation()
                            .andExpression("count / total * 100").as("probability") // 일반 번호 확률 계산
                            .andExpression("bonusCount / total * 100").as("bonusProbability") // 보너스 번호 확률 계산
            );

            // 집계 실행
            AggregationResults<StatLotto> results = mongoTemplate.aggregate(aggregation, "result", StatLotto.class);

            List<StatLottoDTO> statLottoDTOList = new ArrayList<>();

            // 결과 반환
            results.getMappedResults().forEach(s -> {
                statLottoDTOList.add(s.toDTO());
            });

            return statLottoDTOList;
    }

    @Transactional
    public StatLottoDTO readByNumber(Integer number) {
        boolean flag = statLottoRepository.existsByNumber(number);

        if(!flag) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_STAT_LOTTO);
        }

        return statLottoRepository.findByNumber(number).toDTO();
    }

    @Transactional
    public List<StatLottoDTO> readByNumberList(List<Integer> numberList) {
        List<StatLotto> statLottoList = statLottoRepository.findByNumberIn(numberList);

        if(statLottoList.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_STAT_LOTTO);
        }

        List<StatLottoDTO> statLottoDTOList = new ArrayList<>();
        statLottoList.forEach(s -> {
            statLottoDTOList.add(s.toDTO());
        });

        return statLottoDTOList;
    }

    @Transactional
    public List<StatLottoDTO> readAll() {
        List<StatLotto> statLottoList = statLottoRepository.findAll();

        if(statLottoList.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_STAT_LOTTO_LIST);
        }

        List<StatLottoDTO> statLottoDTOList = new ArrayList<>();
        statLottoList.forEach(s -> {
            statLottoDTOList.add(s.toDTO());
        });

        return statLottoDTOList;
    }
}
