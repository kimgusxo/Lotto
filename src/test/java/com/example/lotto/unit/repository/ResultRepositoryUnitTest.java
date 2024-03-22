package com.example.lotto.unit.repository;

import com.example.lotto.domain.Result;
import com.example.lotto.repository.ResultRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
public class ResultRepositoryUnitTest {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Nested
    @DisplayName("find 테스트")
    class Test_Find {

        @BeforeEach
        @DisplayName("데이터 생성")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            Optional<Result> existingResult = Optional.ofNullable(resultRepository.findByRound(round));

            if(existingResult.isEmpty()) {
                Result result = Result.builder()
                        .round(round)
                        .numbers(numbers)
                        .bonusNumber(bonusNumber)
                        .date(date)
                        .build();

                resultRepository.save(result);
            }
        }

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
                Integer bonusNumber = 4;

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
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

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
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            Result result = Result.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();

            resultRepository.insert(result);

            // when & then
            assertThatThrownBy(() -> resultRepository.insert(result))
                    .isInstanceOf(DuplicateKeyException.class);

        }

    }

    @Nested
    @DisplayName("update 테스트")
    class Test_Update {

        @BeforeEach
        @DisplayName("데이터 생성")
        void setUp() {
            // Test DB는 Index 설정을 별도로 해주어야 함
            IndexOperations indexOps = mongoTemplate.indexOps(Result.class);
            indexOps.ensureIndex(new Index().on("round", Sort.Direction.ASC).unique());

            Integer round1 = 1111;
            Integer round2 = 1112;

            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            Optional<Result> existingResult1 = Optional.ofNullable(resultRepository.findByRound(round1));

            if(existingResult1.isEmpty()) {
                Result result1 = Result.builder()
                        .round(round1)
                        .numbers(numbers)
                        .bonusNumber(bonusNumber)
                        .date(date)
                        .build();

                resultRepository.save(result1);
            }

            Optional<Result> existingResult2 = Optional.ofNullable(resultRepository.findByRound(round2));

            if(existingResult2.isEmpty()) {
                Result result2 = Result.builder()
                        .round(round2)
                        .numbers(numbers)
                        .bonusNumber(bonusNumber)
                        .date(date)
                        .build();

                resultRepository.save(result2);
            }
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;
            Result result = resultRepository.findByRound(round);

            Integer updateRound = 1113;
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

    @Nested
    @DisplayName("delete 테스트")
    class Test_Delete {

        @BeforeEach
        @DisplayName("데이터 생성")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            Optional<Result> existingResult = Optional.ofNullable(resultRepository.findByRound(round));

            if(existingResult.isEmpty()) {
                Result result = Result.builder()
                        .round(round)
                        .numbers(numbers)
                        .bonusNumber(bonusNumber)
                        .date(date)
                        .build();

                resultRepository.save(result);
            }
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;

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
