package com.example.lotto.unit.repository;

import com.example.lotto.domain.Result;
import com.example.lotto.repository.ResultRepository;
import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                assertThat(result.getRound())
                        .isEqualTo(round);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer round = -1;

                // when & then
                assertThatThrownBy(() -> resultRepository.findByRound(round))
                        .isInstanceOf(NoSuchElementException.class)
                        .hasMessage(round + "번 회차는 존재하지 않습니다.");
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
                assertThat(result.getBonusNumber())
                        .isEqualTo(bonusNumber);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer bonusNumber = -1;

                // when & then
                assertThatThrownBy(() -> resultRepository.findByBonusNumber(bonusNumber))
                        .isInstanceOf(NoSuchElementException.class)
                        .hasMessage(bonusNumber + "번 보너스 번호는 존재하지 않습니다.");

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
                assertThat(resultList)
                        .isNotEmpty();
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = -1;

                // when
                List<Result> resultList = resultRepository.findByNumbersContaining(number);

                // then
                assertThat(resultList)
                        .isEmpty();

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
                assertThat(resultList)
                        .isNotEmpty();

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
                assertThat(resultList)
                        .isEmpty();

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
            Integer round = 0;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
            Integer bonusNumber = 0;
            LocalDate date = LocalDate.of(2000, 1, 1);

            Result result = Result.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();

            // when
            Result savedResult = resultRepository.insert(result);

            // then
            assertThat(savedResult.getRound())
                    .isEqualTo(round);

        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
            Integer bonusNumber = 0;
            LocalDate date = LocalDate.of(2000, 1, 1);

            Result result = Result.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();

            // when & then
            assertThatThrownBy(() -> resultRepository.insert(result))
                    .isInstanceOf(DuplicateKeyException.class)
                    .hasMessage(round + "회차는 이미 존재합니다.");

        }

    }

    @Nested
    @DisplayName("update 테스트")
    class Test_Update {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;
            Result result = resultRepository.findByRound(round);

            Integer updateRound = 1112;
            result.setRound(updateRound);

            // when
            Result updateResult = resultRepository.insert(result);

            // then
            assertThat(updateResult.getRound())
                    .isEqualTo(updateRound);

        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = -1;

            // when & then
            assertThatThrownBy(() -> {
                Result result = resultRepository.findByRound(round);
                resultRepository.insert(result);
            }).isInstanceOf(NoSuchElementException.class)
                    .hasMessage(round + "번째 회차는 존재하지 않아 수정할 수 없습니다.");


        }

    }

    @Nested
    @DisplayName("delete 테스트")
    class Test_Delete {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;

            // when
            resultRepository.deleteByRound(round);

            // then
            assertThat(resultRepository.findByRound(round))
                    .isNull();
        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = -1;

            // when & then
            assertThatThrownBy(() -> resultRepository.deleteByRound(round))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(round + "회차가 존재하지 않아 삭제할 수 없습니다.");

        }

    }

}
