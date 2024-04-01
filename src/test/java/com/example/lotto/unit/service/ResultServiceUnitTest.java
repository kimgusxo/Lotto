package com.example.lotto.unit.service;

import com.example.lotto.domain.Result;
import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.ResultRepository;
import com.example.lotto.service.ResultService;
import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ResultServiceUnitTest {

    @Mock
    private ResultRepository resultRepository;

    @InjectMocks
    private ResultService resultService;


    @Nested
    @DisplayName("Read 테스트")
    class Test_Read {

        private Result result;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            result = Result.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();
        }

        @Nested
        @DisplayName("readByRound 테스트")
        class Test_ReadByRound {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = 1111;
                given(resultRepository.existsByRound(round)).willReturn(false);
                given(resultRepository.findByRound(round)).willReturn(result);

                // when
                ResultDTO resultDTO = resultService.readByRound(round);

                // then
                assertThat(resultDTO.getRound())
                        .isEqualTo(round);

                then(resultRepository).should(times(1)).existsByRound(round);
                then(resultRepository).should(times(1)).findByRound(round);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer round = -1;
                given(resultRepository.existsByRound(round)).willReturn(true);

                // when & then
                assertThatThrownBy(() -> resultService.readByRound(round))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_ROUND_TOKEN);

                then(resultRepository).should(times(1)).existsByRound(round);
                then(resultRepository).should(times(0)).findByRound(round);
            }
        }

        @Nested
        @DisplayName("readByBonusNumber 테스트")
        class Test_ReadByBonusNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer bonusNumber = 4;
                List<Result> resultList = new ArrayList<>(Arrays.asList(result));

                given(resultRepository.findByBonusNumber(bonusNumber)).willReturn(resultList);

                // when
                List<ResultDTO> resultDTOList = resultService.readByBonusNumber(bonusNumber);

                // then
                assertThat(resultDTOList)
                        .isNotEmpty();

                then(resultRepository).should(times(1)).findByBonusNumber(bonusNumber);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer bonusNumber = -1;
                List<Result> resultList = new ArrayList<>();

                given(resultRepository.findByBonusNumber(bonusNumber)).willReturn(resultList);

                // when & then
                assertThatThrownBy(() -> resultService.readByBonusNumber(bonusNumber))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_BONUS_NUMBER_TOKEN);

                then(resultRepository).should(times(1)).findByBonusNumber(bonusNumber);
            }
        }

        @Nested
        @DisplayName("readByNumber 테스트")
        class Test_ReadByNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 45;
                List<Result> resultList = new ArrayList<>(Arrays.asList(result));

                given(resultRepository.findByNumbersContaining(number)).willReturn(resultList);

                // when
                List<ResultDTO> resultDTOList = resultService.readByNumber(number);

                // then
                assertThat(resultDTOList)
                        .isNotEmpty();

                then(resultRepository).should(times(1)).findByNumbersContaining(number);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer number = -1;
                List<Result> resultList = new ArrayList<>();

                given(resultRepository.findByNumbersContaining(number)).willReturn(resultList);

                // when & then
                assertThatThrownBy(() -> resultService.readByNumber(number))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_NUMBER_TOKEN);

                then(resultRepository).should(times(1)).findByNumbersContaining(number);
            }
        }

        @Nested
        @DisplayName("readByDate 테스트")
        class Test_ReadByDate {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");

                List<Result> resultList = new ArrayList<>(Arrays.asList(result));

                given(resultRepository.findByDateBetween(startDate, endDate)).willReturn(resultList);

                // when
                List<ResultDTO> resultDTOList = resultService.readByDate(startDate, endDate);

                // then
                assertThat(resultDTOList)
                        .isNotEmpty();

                then(resultRepository).should(times(1)).findByDateBetween(startDate, endDate);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1000-01-01");
                LocalDate endDate = LocalDate.parse("1000-01-02");

                List<Result> resultList = new ArrayList<>();

                given(resultRepository.findByDateBetween(startDate, endDate)).willReturn(resultList);

                // when & then
                assertThatThrownBy(() -> resultService.readByDate(startDate, endDate))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INCORRECT_DATE_TOKEN);

                then(resultRepository).should(times(1)).findByDateBetween(startDate, endDate);
            }
        }

    }

    @Nested
    @DisplayName("insert 테스트")
    class Test_Insert {

        private ResultDTO resultDTO;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            resultDTO = ResultDTO.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Result result = resultDTO.toEntity();

            given(resultRepository.existsByRound(resultDTO.getRound())).willReturn(false);
            given(resultRepository.insert(result)).willReturn(result);

            // when
            ResultDTO savedResultDTO = resultService.insert(resultDTO);

            // then
            assertThat(savedResultDTO)
                    .isEqualTo(resultDTO);

            then(resultRepository).should(times(1)).insert(result);
        }

        @Test
        @DisplayName("실패(Repository 예외)")
        void fail_repositoryException() {
            // given
            Result result = resultDTO.toEntity();

            given(resultRepository.existsByRound(resultDTO.getRound())).willReturn(false);
            given(resultRepository.insert(result)).willThrow(DuplicateKeyException.class);

            // when & then
            assertThatThrownBy(() -> resultService.insert(resultDTO))
                    .isInstanceOf(DuplicateKeyException.class);

            then(resultRepository).should(times(1)).existsByRound(resultDTO.getRound());
            then(resultRepository).should(times(1)).insert(result);
        }

        @Test
        @MockitoSettings(strictness = Strictness.LENIENT)
        @DisplayName("실패(Service 예외)")
        void fail_serviceException() {
            // given
            Result result = resultDTO.toEntity();

            given(resultRepository.existsByRound(resultDTO.getRound())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> resultService.insert(resultDTO))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_ROUND_TOKEN);

            then(resultRepository).should(times(1)).existsByRound(resultDTO.getRound());
            then(resultRepository).should(times(0)).insert(result);
        }
    }

    @Nested
    @DisplayName("update 테스트")
    class Test_Update {

        private ResultDTO resultDTO;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            resultDTO = ResultDTO.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer updateRound = 1112;

            Result result = resultDTO.toEntity();
            result.setRound(updateRound);

            given(resultRepository.findByRound(result.getRound())).willReturn(result);
            given(resultRepository.save(result)).willReturn(result);

            // when
            ResultDTO updatedResultDTO = resultService.update(updateRound, resultDTO);

            // then
            assertThat(updatedResultDTO.getRound())
                    .isEqualTo(updateRound);

            then(resultRepository).should(times(1)).findByRound(result.getRound());
            then(resultRepository).should(times(1)).save(result);
        }

        @Test
        @DisplayName("실패(Repository 예외)")
        void fail_duplication() {
            // given
            Integer updateRound = 1112;

            Result result = resultDTO.toEntity();
            result.setRound(updateRound);

            given(resultRepository.findByRound(updateRound)).willReturn(result);
            given(resultRepository.save(result)).willThrow(DuplicateKeyException.class);

            // when & then
            assertThatThrownBy(() -> resultService.update(updateRound, resultDTO))
                    .isInstanceOf(DuplicateKeyException.class);


            then(resultRepository).should(times(1)).findByRound(result.getRound());
            then(resultRepository).should(times(1)).save(result);

        }

        @Test
        @DisplayName("실패(Service 예외)")
        void fail_null() {
            // given
            ResultDTO resultDTO = ResultDTO.builder()
                    .round(0)
                    .numbers(Arrays.asList(0, 0, 0, 0, 0, 0))
                    .bonusNumber(0)
                    .date(LocalDate.now())
                    .build();

            Result result = resultDTO.toEntity();

            given(resultRepository.findByRound(result.getRound())).willReturn(null);

            // when & then
            assertThatThrownBy(() -> resultService.update(result.getRound(), resultDTO))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_RESULT_TOKEN);

            then(resultRepository).should(times(1)).findByRound(result.getRound());
            then(resultRepository).should(times(0)).save(result);
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

            given(resultRepository.deleteByRound(round)).willReturn(1);

            // when
            resultService.delete(round);

            // then
            then(resultRepository).should(times(1)).deleteByRound(round);

        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = 0;

            given(resultRepository.deleteByRound(round)).willReturn(0);

            // when & then
            assertThatThrownBy(() -> resultService.delete(round))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_RESULT_TOKEN);

            then(resultRepository).should(times(1)).deleteByRound(round);
        }
    }

}
