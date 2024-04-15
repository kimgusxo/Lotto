package com.example.lotto.unit.service;

import com.example.lotto.domain.StatLotto;
import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.service.StatLottoService;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class StatLottoServiceUnitTest {

    @Mock
    private MongoTemplate mongoTemplate;
    @InjectMocks
    private StatLottoService statLottoService;

    private StatLotto statLotto;

    @Nested
    @DisplayName("집계쿼리 테스트")
    class Test_Aggregation {

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer number = 1;
            Integer count = 1;
            Double probability = 1.11111;
            Integer bonusCount = 1;
            Double bonusProbability = 1.11111;

            statLotto = StatLotto.builder()
                    .number(number)
                    .count(count)
                    .probability(probability)
                    .bonusCount(bonusCount)
                    .bonusProbability(bonusProbability)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            List<StatLotto> statLottoList = new ArrayList<>(Arrays.asList(statLotto));
            AggregationResults<StatLotto> results = new AggregationResults<>(statLottoList, new Document());

            given(mongoTemplate.aggregate(any(Aggregation.class), eq("result"), eq(StatLotto.class))).willReturn(results);
            // when
            List<StatLottoDTO> calcStatLottoList = statLottoService.calcStatLotto();

            // then
            assertThat(calcStatLottoList).isNotNull();
            assertThat(calcStatLottoList).hasSize(statLottoList.size());
            assertThat(calcStatLottoList.get(0).getNumber()).isEqualTo(statLottoList.get(0).getNumber());
            assertThat(calcStatLottoList.get(0).getCount()).isEqualTo(statLottoList.get(0).getCount());
            assertThat(calcStatLottoList.get(0).getProbability()).isEqualTo(statLottoList.get(0).getProbability());
            assertThat(calcStatLottoList.get(0).getBonusCount()).isEqualTo(statLottoList.get(0).getBonusCount());
            assertThat(calcStatLottoList.get(0).getBonusProbability()).isEqualTo(statLottoList.get(0).getBonusProbability());
        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            given(mongoTemplate.aggregate(any(Aggregation.class), eq("result"), eq(StatLotto.class))).willThrow(DataAccessResourceFailureException.class);

            // when & then
            assertThatThrownBy(() -> statLottoService.calcStatLotto())
                    .isInstanceOf(DataAccessResourceFailureException.class);

        }

    }

}
