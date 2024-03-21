package com.example.lotto.unit.repository;

import com.example.lotto.domain.Result;
import com.example.lotto.repository.ResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
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

                // when
                Result result = resultRepository.findByRound(round);

                // then
                assertThat(result)
                        .isNull();
            }

        }

        @Nested
        @DisplayName("findByBonusNumber 테스트")
        class Test_FindByBonusNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer bonusNumber = 0;

                // when
                List<Result> resultList = resultRepository.findByBonusNumber(bonusNumber);

                // then
                assertThat(resultList)
                        .isNotEmpty();

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer bonusNumber = -1;

                // when
                List<Result> resultList = resultRepository.findByBonusNumber(bonusNumber);

                // then
                assertThat(resultList)
                        .isEmpty();

            }

        }

        @Nested
        @DisplayName("findByNumbersContaining 테스트")
        class Test_FindByNumbersContaining {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 0;

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
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
            Integer bonusNumber = 0;
            LocalDate date = LocalDate.of(2024, 3, 2);

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
            LocalDate date = LocalDate.of(2024, 3, 2);

            Result result = Result.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();

            // when & then
            assertThatThrownBy(() -> resultRepository.insert(result))
                    .isInstanceOf(DuplicateKeyException.class);

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
            Result updateResult = resultRepository.save(result);

            // then
            assertThat(updateResult.getRound())
                    .isEqualTo(updateRound);

        }

        @Test
        @DisplayName("실패(없는 문서를 업데이트 시)")
        void fail_null() {
            // given
            Integer round = -1;

            // when & then
            assertThatThrownBy(() -> {
                Result result = resultRepository.findByRound(round);
                result.setRound(round);
                resultRepository.save(result);
            }).isInstanceOf(NullPointerException.class);

        }

        @Test
        @DisplayName("실패(중복 문서로 업데이트 시)")
        void fail_duplication() {
            // given
            Integer round = 1111;
            Integer updateRound = 1112;

            // when & then
            assertThatThrownBy(() ->{
                        Result result = resultRepository.findByRound(round);
                        result.setRound(updateRound);
                        resultRepository.save(result);
            }).isInstanceOf(DuplicateKeyException.class);

        }

    }
    
    // delete만 해결하면 됨

    @Nested
    @DisplayName("delete 테스트")
    class Test_Delete {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1112;

            // when
            Integer deleteCount = resultRepository.deleteByRound(round);

            // then
            assertThat(deleteCount)
                    .isNotZero();
        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = -1;

            // when
            Integer deleteCount = resultRepository.deleteByRound(round);

            // then
            assertThat(deleteCount)
                    .isZero();

        }

    }

}
