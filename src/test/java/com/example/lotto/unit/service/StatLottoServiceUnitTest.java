package com.example.lotto.unit.service;

import com.example.lotto.domain.StatLotto;
import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.StatLottoRepository;
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

    @Mock
    private StatLottoRepository statLottoRepository;

    @InjectMocks
    private StatLottoService statLottoService;

    private StatLotto statLotto;

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

    @Nested
    @DisplayName("집계쿼리 테스트")
    class Test_Aggregation {

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

    @Nested
    @DisplayName("read 테스트")
    class Test_Read {

        @Nested
        @DisplayName("readByNumber 테스트")
        class Test_ReadByNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 1;
                given(statLottoRepository.existsByNumber(number)).willReturn(true);
                given(statLottoRepository.findByNumber(number)).willReturn(statLotto);

                // when
                StatLottoDTO statLottoDTO = statLottoService.readByNumber(number);

                // then
                assertThat(statLottoDTO.getNumber())
                        .isEqualTo(number);

                then(statLottoRepository).should(times(1)).existsByNumber(number);
                then(statLottoRepository).should(times(1)).findByNumber(number);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = 1;
                given(statLottoRepository.existsByNumber(number)).willReturn(false);

                // when & then
                assertThatThrownBy(() -> statLottoService.readByNumber(number))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_STAT_LOTTO);

                then(statLottoRepository).should(times(1)).existsByNumber(number);
                then(statLottoRepository).should(times(0)).findByNumber(number);
            }

        }

        @Nested
        @DisplayName("readByNumberList 테스트")
        class Test_ReadByNumberList {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3));
                List<StatLotto> statLottoList = new ArrayList<>(Arrays.asList(statLotto));

                given(statLottoRepository.findByNumberIn(numberList)).willReturn(statLottoList);

                // when
                List<StatLottoDTO> statLottoDTOList = statLottoService.readByNumberList(numberList);

                // then
                assertThat(statLottoDTOList)
                        .isNotEmpty();

                then(statLottoRepository).should(times(1)).findByNumberIn(numberList);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3));
                List<StatLotto> statLottoList = new ArrayList<>();

                given(statLottoRepository.findByNumberIn(numberList)).willReturn(statLottoList);

                // when & then
                assertThatThrownBy(() -> statLottoService.readByNumberList(numberList))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_STAT_LOTTO);

                then(statLottoRepository).should(times(1)).findByNumberIn(numberList);

            }
        }

        @Nested
        @DisplayName("readAll 테스트")
        class Test_ReadAll {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                List<StatLotto> statLottoList = new ArrayList<>(Arrays.asList(statLotto));

                given(statLottoRepository.findAll()).willReturn(statLottoList);

                // when
                List<StatLottoDTO> statLottoDTOList = statLottoService.readAll();

                // then
                assertThat(statLottoDTOList)
                        .isNotEmpty();


                then(statLottoRepository).should(times(1)).findAll();
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                List<StatLotto> statLottoList = new ArrayList<>();

                given(statLottoRepository.findAll()).willReturn(statLottoList);

                // when & then
                assertThatThrownBy(() -> statLottoService.readAll())
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_STAT_LOTTO);

                then(statLottoRepository).should(times(1)).findAll();

            }
        }
    }

}
