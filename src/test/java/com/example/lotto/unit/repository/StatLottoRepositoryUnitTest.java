package com.example.lotto.unit.repository;

import com.example.lotto.domain.StatLotto;
import com.example.lotto.repository.StatLottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class StatLottoRepositoryUnitTest {

    @Autowired
    private StatLottoRepository statLottoRepository;

    @Nested
    @DisplayName("find 테스트")
    class Test_Find {

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer number = 1;
            Integer count = 1;
            Double probability = 1.1111;
            Integer bonusCount = 1;
            Double bonusProbability = 1.1111;

            Optional<StatLotto> existingStatLotto = Optional.ofNullable(statLottoRepository.findByNumber(number));

            if(existingStatLotto.isEmpty()) {
                StatLotto statLotto = StatLotto.builder()
                        .number(number)
                        .count(count)
                        .probability(probability)
                        .bonusCount(bonusCount)
                        .bonusProbability(bonusProbability)
                        .build();

                statLottoRepository.save(statLotto);
            }
        }

        @Nested
        @DisplayName("findByNumber 테스트")
        class Test_FindByNumber {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 1;

                // when
                StatLotto statLotto = statLottoRepository.findByNumber(number);

                // then
                assertThat(statLotto.getNumber())
                        .isEqualTo(number);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = 0;

                // when
                StatLotto statLotto = statLottoRepository.findByNumber(number);

                // then
                assertThat(statLotto)
                        .isNull();
            }
        }

        @Nested
        @DisplayName("findByNumberIn 테스트")
        class Test_FindByNumberIn {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 1;
                List<Integer> numberList = new ArrayList<>(Arrays.asList(number));

                // when
                List<StatLotto> statLottoList = statLottoRepository.findByNumberIn(numberList);

                // then
                assertThat(statLottoList)
                        .isNotEmpty();
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = 0;
                List<Integer> numberList = new ArrayList<>(Arrays.asList(number));

                // when
                List<StatLotto> statLottoList = statLottoRepository.findByNumberIn(numberList);

                // then
                assertThat(statLottoList)
                        .isEmpty();

            }

        }

        @Nested
        @DisplayName("findAll 테스트")
        class Test_FindAll {
            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when
                List<StatLotto> statLottoList = statLottoRepository.findAll();

                // then
                assertThat(statLottoList)
                        .isNotEmpty();
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                statLottoRepository.deleteAll();

                // when
                List<StatLotto> statLottoList = statLottoRepository.findAll();

                // then
                assertThat(statLottoList)
                        .isEmpty();
            }
        }

    }


}
