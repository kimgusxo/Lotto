package com.example.lotto.integration;

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

    private ResultDTO resultDTO;

    @BeforeEach
    @DisplayName("데이터 생성")
    void setUp() {

        Integer round = 1111;
        List<Integer> numbers = Arrays.asList(3, 13, 30, 33, 43, 45);
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
    @DisplayName("GET API 테스트")
    class Test_GET {

        @BeforeEach
        @DisplayName("데이터 생성")
        void create() {
            resultRepository.save(resultDTO.toEntity());
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

                ResultDTO findResultDTO = response.getBody();

                // then
                resultAssertThat(response, resultDTO, findResultDTO, HttpStatus.OK);

                // log
                logger(resultDTO);
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
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_ROUND;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

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
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
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

                List<ResultDTO> resultDTOList = Arrays.asList(response.getBody());

                // then
                resultListAssertThat(response, resultDTO, resultDTOList, HttpStatus.OK);

                // log
                logger(resultDTOList.get(0));

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
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_NUMBER;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidNumber = -1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/number/" + inValidNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);
                // log
                errorLogger(errorDTO);
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

                List<ResultDTO> resultDTOList = Arrays.asList(response.getBody());

                // then
                resultListAssertThat(response, resultDTO, resultDTOList, HttpStatus.OK);

                // log
                logger(resultDTOList.get(0));
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
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_BONUS_NUMBER;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidBonusNumber = -1;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/result/get/bonusNumber/" + inValidBonusNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
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
                String url = "/result/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<ResultDTO[]> response =
                        testRestTemplate.getForEntity(url, ResultDTO[].class);

                List<ResultDTO> resultDTOList = Arrays.asList(response.getBody());

                // then
                resultListAssertThat(response, resultDTO, resultDTOList, HttpStatus.OK);

                // log
                logger(resultDTOList.get(0));
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1999-03-01");
                LocalDate endDate = LocalDate.parse("1999-03-31");
                String url = "/result/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.INCORRECT_RESULT_DATE;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                LocalDate startDate = LocalDate.parse("9999-03-01");
                LocalDate endDate = LocalDate.parse("9999-03-31");
                String url = "/result/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }
        }
    }

    @Nested
    @DisplayName("POST API 테스트")
    class Test_POST {

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
                resultAssertThat(response, resultDTO, savedResultDTO, HttpStatus.CREATED);

                // log
                logger(savedResultDTO);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                ResultDTO existResult = resultRepository.save(resultDTO.toEntity()).toDTO();

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/result/post/insert", existResult, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.DUPLICATE_RESULT_ROUND;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

                @Test
                @DisplayName("실패(Validation_Numbers 예외)")
                void fail_valid_numbers() {
                    // given
                    List<Integer> inValidNumbers = Arrays.asList(1, 2, 3, 4, 5);
                    resultDTO.setNumbers(inValidNumbers);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

                @Test
                @DisplayName("실패(Validation_Number 예외)")
                void fail_valid_number() {
                    // given
                    List<Integer> inValidNumbers = Arrays.asList(1, 2, 3, 4, 5, 999);
                    resultDTO.setNumbers(inValidNumbers);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/result/post/insert", resultDTO, ErrorDTO.class);

                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

            }

        }

    }

    @Nested
    @DisplayName("PUT API 테스트")
    class Test_PUT {

        @BeforeEach
        @DisplayName("데이터 생성")
        void create() {
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
                resultAssertThat(response, resultDTO, updatedResultDTO, HttpStatus.ACCEPTED);

                // log
                logger(updatedResultDTO);

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
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

                @Test
                @DisplayName("실패(Validation_Numbers 예외)")
                void fail_valid_numbers() {
                    // given
                    List<Integer> inValidNumbers = Arrays.asList(1, 2, 3, 4, 5);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);
                    // log
                    errorLogger(errorDTO);
                }

                @Test
                @DisplayName("실패(Validation_Number 예외)")
                void fail_valid_number() {
                    // given
                    List<Integer> inValidNumbers = Arrays.asList(1, 2, 3, 4, 5, 999);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
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
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

            }

        }

    }

    @Nested
    @DisplayName("DELETE API 테스트")
    class Test_DELETE {

        @BeforeEach
        @DisplayName("데이터 생성")
        void create() {
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
                        "/result/delete/" + round,
                        HttpMethod.DELETE,
                        null,
                        Void.class);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

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
                        "/result/delete/" + notExistRound,
                        HttpMethod.DELETE,
                        null,
                        ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() {
                // given
                Integer inValidRound = -1;

                // when
                ResponseEntity<ErrorDTO> errorResponse = testRestTemplate.exchange(
                        "/result/delete/" + inValidRound,
                        HttpMethod.DELETE,
                        null,
                        ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }
        }

    }

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        resultRepository.deleteAll();
    }

    // result assertThat 함수
    void resultAssertThat(ResponseEntity<ResultDTO> response, ResultDTO inputResultDTO,
                          ResultDTO outputResultDTO, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputResultDTO.getRound())
                .isEqualTo(inputResultDTO.getRound());
        assertThat(outputResultDTO.getNumbers())
                .isEqualTo(inputResultDTO.getNumbers());
        assertThat(outputResultDTO.getBonusNumber())
                .isEqualTo(inputResultDTO.getBonusNumber());
        assertThat(outputResultDTO.getDate())
                .isEqualTo(inputResultDTO.getDate());
    }

    // resultList assertThat 함수
    void resultListAssertThat(ResponseEntity<ResultDTO[]> response, ResultDTO inputResultDTO,
                              List<ResultDTO> outputResultDTOList, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputResultDTOList)
                .isNotEmpty();

        assertThat(outputResultDTOList.get(0).getRound())
                .isEqualTo(inputResultDTO.getRound());
        assertThat(outputResultDTOList.get(0).getNumbers())
                .isEqualTo(inputResultDTO.getNumbers());
        assertThat(outputResultDTOList.get(0).getBonusNumber())
                .isEqualTo(inputResultDTO.getBonusNumber());
        assertThat(outputResultDTOList.get(0).getDate())
                .isEqualTo(inputResultDTO.getDate());
    }

    // error assertThat 함수
    void errorAssertThat(ResponseEntity<ErrorDTO> errorResponse, ErrorDTO errorDTO, ErrorCode errorCode) {
        assertThat(errorResponse)
                .isNotNull();

        assertThat(errorResponse.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(errorDTO.getCode())
                    .isEqualTo(errorCode.getCode());
        assertThat(errorDTO.getDetail())
                .isEqualTo(errorCode.getDetail());
    }

    // 정상 일때 logger
    void logger(ResultDTO resultDTO) {
        log.info("round: {}", resultDTO.getRound());
        log.info("numbers: {}", resultDTO.getNumbers());
        log.info("bonusNumber: {}", resultDTO.getBonusNumber());
        log.info("date: {}", resultDTO.getDate());
    }

    // 에러 일때 logger
    void errorLogger(ErrorDTO errorDTO) {
        log.info("code: {}", errorDTO.getCode());
        log.info("detail: {}", errorDTO.getDetail());
    }


}
