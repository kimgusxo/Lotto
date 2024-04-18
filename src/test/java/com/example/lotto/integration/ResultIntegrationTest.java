package com.example.lotto.integration;

import com.example.lotto.domain.Result;
import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.error.ErrorDTO;
import com.example.lotto.repository.ResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResultIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ResultRepository resultRepository;

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        private Result result;

        @BeforeEach
        @DisplayName("데이터 생성")
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

            resultRepository.save(result);
        }

        @Nested
        @DisplayName("getByRound 테스트")
        class Test_GetByRound {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = 1111;

                // when
                ResponseEntity<ResultDTO> response
                        = testRestTemplate.getForEntity("/result/get/round/" + round, ResultDTO.class);

                ResultDTO resultDTO = response.getBody();

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.OK);

                assertThat(resultDTO.getRound())
                        .isEqualTo(result.getRound());
                assertThat(resultDTO.getNumbers())
                        .isEqualTo(result.getNumbers());
                assertThat(resultDTO.getBonusNumber())
                        .isEqualTo(result.getBonusNumber());
                assertThat(resultDTO.getDate())
                        .isEqualTo(result.getDate());

                log.info("round: {}", resultDTO.getRound());
                log.info("numbers: {}", resultDTO.getNumbers());
                log.info("bonusNumber: {}", resultDTO.getBonusNumber());
                log.info("date: {}", resultDTO.getDate());
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 9999;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/round/" + notExistRound, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_ROUND.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_ROUND.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidRound = -1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/round/" + inValidRound, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.VALIDATION.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.VALIDATION.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }
        }

        @Nested
        @DisplayName("getByNumber 테스트")
        class Test_GetByNumber {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 3;

                // when
                ResponseEntity<ResultDTO[]> response =
                        testRestTemplate.getForEntity("/result/get/number/" + number, ResultDTO[].class);

                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(response.getBody()));

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.OK);

                assertThat(resultDTOList)
                        .isNotEmpty();
                assertThat(resultDTOList.get(0).getRound())
                        .isEqualTo(result.getRound());
                assertThat(resultDTOList.get(0).getNumbers())
                        .isEqualTo(result.getNumbers());
                assertThat(resultDTOList.get(0).getBonusNumber())
                        .isEqualTo(result.getBonusNumber());
                assertThat(resultDTOList.get(0).getDate())
                        .isEqualTo(result.getDate());


                log.info("round: {}", resultDTOList.get(0).getRound());
                log.info("numbers: {}", resultDTOList.get(0).getNumbers());
                log.info("bonusNumber: {}", resultDTOList.get(0).getBonusNumber());
                log.info("date: {}", resultDTOList.get(0).getDate());

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistNumber = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/number/" + notExistNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_NUMBER.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_NUMBER.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidNumber = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/number/" + inValidNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.VALIDATION.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.VALIDATION.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }

        }

        @Nested
        @DisplayName("getByBonusNumber 테스트")
        class Test_GetByBonusNumber {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer bonusNumber = 4;

                // when
                ResponseEntity<ResultDTO[]> response =
                        testRestTemplate.getForEntity("/result/get/bonusNumber/" + bonusNumber, ResultDTO[].class);

                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(response.getBody()));

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.OK);

                assertThat(resultDTOList)
                        .isNotEmpty();
                assertThat(resultDTOList.get(0).getRound())
                        .isEqualTo(result.getRound());
                assertThat(resultDTOList.get(0).getNumbers())
                        .isEqualTo(result.getNumbers());
                assertThat(resultDTOList.get(0).getBonusNumber())
                        .isEqualTo(result.getBonusNumber());
                assertThat(resultDTOList.get(0).getDate())
                        .isEqualTo(result.getDate());

                log.info("round: {}", resultDTOList.get(0).getRound());
                log.info("numbers: {}", resultDTOList.get(0).getNumbers());
                log.info("bonusNumber: {}", resultDTOList.get(0).getBonusNumber());
                log.info("date: {}", resultDTOList.get(0).getDate());
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistBonusNumber = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/bonusNumber/" + notExistBonusNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_BONUS_NUMBER.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_BONUS_NUMBER.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidBonusNumber = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/bonusNumber/" + inValidBonusNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.VALIDATION.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.VALIDATION.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }
        }

        @Nested
        @DisplayName("getByDate 테스트")
        class Test_GetByDate {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");
                String url = String.format("/get/date?startDate=%s&endDate=%s", startDate, endDate);

                // when
                ResponseEntity<ResultDTO[]> response =
                        testRestTemplate.getForEntity(url, ResultDTO[].class);

                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(response.getBody()));

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.OK);

                assertThat(resultDTOList)
                        .isNotEmpty();
                assertThat(resultDTOList.get(0).getRound())
                        .isEqualTo(result.getRound());
                assertThat(resultDTOList.get(0).getNumbers())
                        .isEqualTo(result.getNumbers());
                assertThat(resultDTOList.get(0).getBonusNumber())
                        .isEqualTo(result.getBonusNumber());
                assertThat(resultDTOList.get(0).getDate())
                        .isEqualTo(result.getDate());

                log.info("round: {}", resultDTOList.get(0).getRound());
                log.info("numbers: {}", resultDTOList.get(0).getNumbers());
                log.info("bonusNumber: {}", resultDTOList.get(0).getBonusNumber());
                log.info("date: {}", resultDTOList.get(0).getDate());
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1999-03-01");
                LocalDate endDate = LocalDate.parse("1999-03-31");
                String url = String.format("/get/date?startDate=%s&endDate=%s", startDate, endDate);

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.INCORRECT_RESULT_DATE.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.INCORRECT_RESULT_DATE.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                LocalDate startDate = LocalDate.parse("9999-03-01");
                LocalDate endDate = LocalDate.parse("9999-03-31");
                String url = String.format("/get/date?startDate=%s&endDate=%s", startDate, endDate);

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.VALIDATION.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.VALIDATION.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());
            }
        }
    }

    @Nested
    @DisplayName("POST API 테스트")
    class Test_POST {

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

        @Nested
        @DisplayName("insert 테스트")
        class Test_Insert {
            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when
                ResponseEntity<ResultDTO> response =
                        testRestTemplate.postForEntity("/result/post/insert", resultDTO, ResultDTO.class);

                ResultDTO savedResultDTO = response.getBody();

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);

                assertThat(savedResultDTO.getRound())
                        .isEqualTo(resultDTO.getRound());
                assertThat(savedResultDTO.getNumbers())
                        .isEqualTo(resultDTO.getNumbers());
                assertThat(savedResultDTO.getBonusNumber())
                        .isEqualTo(resultDTO.getBonusNumber());
                assertThat(savedResultDTO.getDate())
                        .isEqualTo(resultDTO.getDate());

                log.info("savedRound: {}", savedResultDTO.getRound());
                log.info("savedNumbers: {}", savedResultDTO.getNumbers());
                log.info("savedBonusNumber: {}", savedResultDTO.getBonusNumber());
                log.info("savedDate: {}", savedResultDTO.getDate());

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                ResultDTO notExistResult = null;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/result/post/insert", notExistResult, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());

            }

            @Nested
            @DisplayName("Validation 테스트")
            class Test_Insert_Valid {

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_round() {
                    // given
                    Integer inValidRound = -1;
                    resultDTO.setRound(inValidRound);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Numbers 예외)")
                void fail_valid_numbers() {
                    // given
                    List<Integer> inValidNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
                    resultDTO.setNumbers(inValidNumbers);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Number 예외)")
                void fail_valid_number() {
                    // given
                    List<Integer> inValidNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 999));
                    resultDTO.setNumbers(inValidNumbers);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_BonusNumber 예외)")
                void fail_valid_bonusNumber() {
                    // given
                    Integer inValidBonusNumber = -1;
                    resultDTO.setBonusNumber(inValidBonusNumber);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Date 예외)")
                void fail_valid_date() {
                    // given
                    LocalDate inValidDate = LocalDate.parse("3000-03-01");
                    resultDTO.setDate(inValidDate);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

            }

        }

    }

    @Nested
    @DisplayName("PUT API 테스트")
    class Test_PUT {

        private ResultDTO resultDTO;

        @BeforeEach
        @DisplayName("데이터 생성")
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

            resultRepository.save(resultDTO.toEntity());

        }

        @Nested
        @DisplayName("update 테스트")
        class Test_Update {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer updateBonusNumber = 10;
                resultDTO.setBonusNumber(updateBonusNumber);

                HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                // when
                ResponseEntity<ResultDTO> response =
                        testRestTemplate.exchange(
                                "/result/put/update/" + resultDTO.getRound(),
                                HttpMethod.PUT,
                                resultDTOHttpEntity,
                                ResultDTO.class
                        );

                ResultDTO updatedResultDTO = response.getBody();

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);

                assertThat(updatedResultDTO.getRound())
                        .isEqualTo(resultDTO.getRound());
                assertThat(updatedResultDTO.getNumbers())
                        .isEqualTo(resultDTO.getNumbers());
                assertThat(updatedResultDTO.getBonusNumber())
                        .isEqualTo(resultDTO.getBonusNumber());
                assertThat(updatedResultDTO.getDate())
                        .isEqualTo(resultDTO.getDate());

                log.info("updatedRound: {}", updatedResultDTO.getRound());
                log.info("updatedNumbers: {}", updatedResultDTO.getNumbers());
                log.info("updatedBonusNumber: {}", updatedResultDTO.getBonusNumber());
                log.info("updatedDate: {}", updatedResultDTO.getDate());

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 1;
                resultDTO.setRound(notExistRound);

                HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.exchange(
                                "/result/put/update/" + resultDTO.getRound(),
                                HttpMethod.PUT,
                                resultDTOHttpEntity,
                                ErrorDTO.class
                        );

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());

            }

            @Nested
            @DisplayName("Validation 테스트")
            class Test_Update_Validation {

                @Test
                @DisplayName("실패(Validation_updateRound 예외)")
                void fail_valid_updateRound() {
                    // given
                    Integer inValidRound = -1;

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + inValidRound,
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_round() {
                    // given
                    Integer validRound = 1111;
                    Integer inValidRound = -1;
                    resultDTO.setRound(inValidRound);

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + validRound,
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Numbers 예외)")
                void fail_valid_numbers() {
                    // given
                    List<Integer> inValidNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
                    resultDTO.setNumbers(inValidNumbers);

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + resultDTO.getRound(),
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Number 예외)")
                void fail_valid_number() {
                    // given
                    List<Integer> inValidNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 999));
                    resultDTO.setNumbers(inValidNumbers);

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + resultDTO.getRound(),
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_BonusNumber 예외)")
                void fail_valid_bonusNumber() {
                    // given
                    Integer inValidBonusNumber = -1;
                    resultDTO.setBonusNumber(inValidBonusNumber);

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + resultDTO.getRound(),
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

                @Test
                @DisplayName("실패(Validation_Date 예외)")
                void fail_valid_date() {
                    // given
                    LocalDate inValidDate = LocalDate.parse("3000-03-01");
                    resultDTO.setDate(inValidDate);

                    HttpEntity<ResultDTO> resultDTOHttpEntity = new HttpEntity<>(resultDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/result/put/update/" + resultDTO.getRound(),
                                    HttpMethod.PUT,
                                    resultDTOHttpEntity,
                                    ErrorDTO.class
                            );

                    ErrorDTO errorDTO = errorResponse.getBody();

                    // then
                    assertThat(errorResponse)
                            .isNotNull();

                    assertThat(errorResponse.getStatusCode())
                            .isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(errorDTO.getCode())
                            .isEqualTo(ErrorCode.VALIDATION.getCode());
                    assertThat(errorDTO.getDetail())
                            .isEqualTo(ErrorCode.VALIDATION.getDetail());

                    log.info("code: {}", errorDTO.getCode());
                    log.info("detail: {}", errorDTO.getDetail());
                }

            }

        }

    }

    @Nested
    @DisplayName("DELETE API 테스트")
    class Test_DELETE {

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

            resultRepository.save(resultDTO.toEntity());
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
                ResponseEntity<Void> response = testRestTemplate.exchange(
                        "/result/delete/round/" + round,
                        HttpMethod.DELETE,
                        null,
                        Void.class);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                boolean isDeleted = resultRepository.existsByRound(round);
                assertThat(isDeleted).isFalse();

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse = testRestTemplate.exchange(
                        "/result/delete/round/" + notExistRound,
                        HttpMethod.DELETE,
                        null,
                        ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_ROUND.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.NOT_EXIST_RESULT_ROUND.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidRound = -1;

                // when
                ResponseEntity<ErrorDTO> errorResponse = testRestTemplate.exchange(
                        "/result/delete/round/" + inValidRound,
                        HttpMethod.DELETE,
                        null,
                        ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();

                // then
                assertThat(errorResponse)
                        .isNotNull();

                assertThat(errorResponse.getStatusCode())
                        .isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(errorDTO.getCode())
                        .isEqualTo(ErrorCode.VALIDATION.getCode());
                assertThat(errorDTO.getDetail())
                        .isEqualTo(ErrorCode.VALIDATION.getDetail());

                log.info("code: {}", errorDTO.getCode());
                log.info("detail: {}", errorDTO.getDetail());

            }
        }

    }

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        resultRepository.deleteAll();
    }

//    // resultList assertThat 함수
//    void resultListAssertThat(List<ResultDTO> resultDTOList) {
//
//    }
//
//    // result assertThat 함수
//    void resultAssertThat() {
//
//    }
//
//    // error assertThat 함수
//    void errorAssertThat() {
//
//    }
//
//    // 정상 일때 logger
//    void logger() {
//
//    }
//
//    // 에러 일때 logger
//    void errorLogger() {
//
//    }


}
