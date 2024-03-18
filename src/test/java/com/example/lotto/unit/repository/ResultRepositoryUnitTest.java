package com.example.lotto.unit.repository;

import com.example.lotto.domain.Result;
import com.example.lotto.repository.ResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class ResultRepositoryUnitTest {

    @Autowired
    private ResultRepository resultRepository;

    @Nested
    @DisplayName("find 테스트")
    class Test_Find {
        @Nested
        @DisplayName("findByRound 테스트")
        class Test_FindByRound {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = 1111;

                // when
                Result result = resultRepository.findByRound(round);

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer round = -1;

                // when
                Result result = resultRepository.findByRound(round);

                // then

            }

        }

        @Nested
        @DisplayName("findByBonusNumber 테스트")
        class Test_FindByBonusNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer bonusNumber = 4;

                // when
                Result result = resultRepository.findByBonusNumber(bonusNumber);

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer bonusNumber = -1;

                // when
                Result result = resultRepository.findByBonusNumber(bonusNumber);

                // then

            }

        }

        @Nested
        @DisplayName("findByNumbersContaining 테스트")
        class Test_FindByNumbersContaining {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 3;

                // when
                List<Result> resultList = resultRepository.findByNumbersContaining(number);

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = -1;

                // when
                List<Result> resultList = resultRepository.findByNumbersContaining(number);

                // then

            }

        }

        @Nested
        @DisplayName("findByDateBetween 테스트")
        class Test_FindByDateBetween {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");

                // when
                List<Result> resultList = resultRepository.findByDateBetween(startDate, endDate);

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1000-03-01");
                LocalDate endDate = LocalDate.parse("1000-03-31");

                // when
                List<Result> resultList = resultRepository.findByDateBetween(startDate, endDate);

                // then
            }

        }
    }

    @Nested
    @DisplayName("insert 테스트")
    class Test_Insert {

        @Test
        @DisplayName("성공")
        void success() {
            // given

            // when

            // then


        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given

            // when

            // then

        }

    }

}
