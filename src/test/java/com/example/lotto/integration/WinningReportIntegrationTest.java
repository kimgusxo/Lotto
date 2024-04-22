package com.example.lotto.integration;

import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.error.ErrorDTO;
import com.example.lotto.repository.WinningReportRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WinningReportIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WinningReportRepository winningReportRepository;

    private WinningReportDTO winningReportDTO;

    private RankDTO rankDTO;

    @BeforeEach
    @DisplayName("데이터 설정")
    void setUp() {
        Integer round = 1111;
        LocalDate date = LocalDate.parse("2024-03-16");
        Long totalWinningAmount = 116382835000L;
        rankDTO = RankDTO.builder()
                .ranking(1)
                .winningCount(16)
                .totalWinningAmount(27434600640L)
                .winningAmount(1714662540L)
                .build();

        List<RankDTO> rankDTOList = Stream.generate(() -> rankDTO)
                .limit(5)
                .collect(Collectors.toList());

        winningReportDTO = WinningReportDTO.builder()
                .round(round)
                .date(date)
                .totalWinningAmount(totalWinningAmount)
                .rankDTOList(rankDTOList)
                .build();
    }

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        @BeforeEach
        @DisplayName("데이터 설정")
        void create() {
            winningReportRepository.save(winningReportDTO.toEntity());
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
                ResponseEntity<WinningReportDTO> response =
                        testRestTemplate.getForEntity("/winningReport/get/round/" + round, WinningReportDTO.class);

                WinningReportDTO findWinningReportDTO = response.getBody();

                // then
                winningReportAssertThat(response, winningReportDTO, findWinningReportDTO, HttpStatus.OK);

                // log
                logger(findWinningReportDTO);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 9999;

                // when
                ResponseEntity<ErrorDTO> errorResponse
                        = testRestTemplate.getForEntity("/winningReport/get/round/" + notExistRound, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT_ROUND;


                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

            @Test
            @DisplayName("실패(Validation 예외")
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
        @DisplayName("getByDate 테스트")
        class Test_GetByDate {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");
                String url = "/winningReport/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<WinningReportDTO[]> response =
                        testRestTemplate.getForEntity(url, WinningReportDTO[].class);

                List<WinningReportDTO> winningReportDTOList = Arrays.asList(response.getBody());

                // then
                winningReportListAssertThat(response, winningReportDTO, winningReportDTOList, HttpStatus.OK);

                // log
                logger(winningReportDTOList.get(0));

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1999-03-01");
                LocalDate endDate = LocalDate.parse("1999-03-31");
                String url = "/winningReport/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.INCORRECT_WINNING_REPORT_DATE;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

            @Test
            @DisplayName("실패(Validation 예외")
            void fail_valid() {
                // given
                LocalDate startDate = LocalDate.parse("9999-03-01");
                LocalDate endDate = LocalDate.parse("9999-03-31");
                String url = "/winningReport/get/date?startDate=" + startDate + "&endDate=" + endDate;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity(url, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.VALIDATION;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }
        }

        @Nested
        @DisplayName("getByTotalWinningAmount 테스트")
        class Test_GetByTotalWinningAmount {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                Long totalWinningAmount = 100000000L;

                // when
                ResponseEntity<WinningReportDTO[]> response =
                        testRestTemplate.getForEntity("/winningReport/get/totalWinningAmount/" + totalWinningAmount, WinningReportDTO[].class);

                List<WinningReportDTO> winningReportDTOList = Arrays.asList(response.getBody());

                // then
                winningReportListAssertThat(response, winningReportDTO, winningReportDTOList, HttpStatus.OK);

                // log
                logger(winningReportDTOList.get(0));

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Long notExistTotalWinningAmount = 9999999999999L;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/winningReport/get/totalWinningAmount/" + notExistTotalWinningAmount, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Test
            @DisplayName("실패(Validation 예외")
            void fail_valid() {
                Long inValidTotalWinningAmount = -1L;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/winningReport/get/totalWinningAmount/" + inValidTotalWinningAmount, ErrorDTO.class);

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
                ResponseEntity<WinningReportDTO> response =
                        testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, WinningReportDTO.class);

                WinningReportDTO savedWinningReportDTO = response.getBody();

                // then
                winningReportAssertThat(response, winningReportDTO, savedWinningReportDTO, HttpStatus.CREATED);

                // log
                logger(savedWinningReportDTO);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                WinningReportDTO existWinningReportDTO = winningReportRepository.save(winningReportDTO.toEntity()).toDTO();

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/winningReport/post/insert", existWinningReportDTO, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.DUPLICATE_WINNING_REPORT_ROUND;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Nested
            @DisplayName("Validation(WinningReport) 테스트")
            class Test_Insert_Valid_WinningReport {

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_round() {
                    // given
                    Integer inValidRound = -1;
                    winningReportDTO.setRound(inValidRound);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


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
                    LocalDate inValidDate = LocalDate.parse("9999-03-01");
                    winningReportDTO.setDate(inValidDate);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);

                }

                @Test
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() {
                    // given
                    Long inValidTotalWinningAmount = -1L;
                    winningReportDTO.setTotalWinningAmount(inValidTotalWinningAmount);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);

                }

                @Test
                @DisplayName("실패(Validation_RankList 예외)")
                void fail_valid_rankList() {
                    // given
                    List<RankDTO> inValidRankDTOList = Arrays.asList(rankDTO);
                    winningReportDTO.setRankDTOList(inValidRankDTOList);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);

                }
            }

            @Nested
            @DisplayName("Validation(Rank) 테스트")
            class Test_Insert_Valid_Rank {

                @Test
                @DisplayName("실패(Validation_Ranking 예외)")
                void fail_valid_ranking() {
                    // given
                    Integer inValidRanking = -1;
                    winningReportDTO.getRankDTOList().get(0).setRanking(inValidRanking);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);

                }

                @Test
                @DisplayName("실패(Validation_WinningCount 예외)")
                void fail_valid_winningCount() {
                    // given
                    Integer inValidWinningCount = -1;
                    winningReportDTO.getRankDTOList().get(0).setWinningCount(inValidWinningCount);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);

                }

                @Test
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() {
                    // given
                    Long inValidTotalWinningAmount = -1L;
                    winningReportDTO.getRankDTOList().get(0).setTotalWinningAmount(inValidTotalWinningAmount);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


                    ErrorDTO errorDTO = errorResponse.getBody();
                    ErrorCode errorCode = ErrorCode.VALIDATION;

                    // then
                    errorAssertThat(errorResponse, errorDTO, errorCode);

                    // log
                    errorLogger(errorDTO);
                }

                @Test
                @DisplayName("실패(Validation_WinningAmount 예외)")
                void fail_valid_winningAmount() {
                    // given
                    Long inValidWinningAmount = -1L;
                    winningReportDTO.getRankDTOList().get(0).setWinningAmount(inValidWinningAmount);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.postForEntity("/winningReport/post/insert", winningReportDTO, ErrorDTO.class);


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
            winningReportRepository.save(winningReportDTO.toEntity());
        }

        @Nested
        @DisplayName("update 테스트")
        class Test_Update {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Long updateTotalWinningAmount = 9999L;
                winningReportDTO.setTotalWinningAmount(updateTotalWinningAmount);

                HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                // when
                ResponseEntity<WinningReportDTO> response =
                        testRestTemplate.exchange(
                                "/winningReport/put/update/" + winningReportDTO.getRound(),
                                HttpMethod.PUT,
                                winningReportDTOHttpEntity,
                                WinningReportDTO.class
                        );

                WinningReportDTO updatedWinningReportDTO = response.getBody();

                // then
                winningReportAssertThat(response, winningReportDTO, updatedWinningReportDTO, HttpStatus.ACCEPTED);


                // log
                logger(updatedWinningReportDTO);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 1;
                winningReportDTO.setRound(notExistRound);

                HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.exchange(
                                "/winningReport/put/update/" + winningReportDTO.getRound(),
                                HttpMethod.PUT,
                                winningReportDTOHttpEntity,
                                ErrorDTO.class
                        );

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

            @Nested
            @DisplayName("Validation(WinningReport) 테스트")
            class Test_Update_Valid_WinningReport {

                @Test
                @DisplayName("실패(Validation_updateRound 예외)")
                void fail_valid_updateRound() {
                    // given
                    Integer inValidRound = -1;

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + inValidRound,
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                    winningReportDTO.setRound(inValidRound);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + validRound,
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                    LocalDate inValidDate = LocalDate.parse("9999-03-01");
                    winningReportDTO.setDate(inValidDate);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() {
                    // given
                    Long inValidTotalWinningAmount = -1L;
                    winningReportDTO.setTotalWinningAmount(inValidTotalWinningAmount);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                @DisplayName("실패(Validation_RankList 예외)")
                void fail_valid_rankList() {
                    // given
                    List<RankDTO> inValidRankDTOList = Arrays.asList(rankDTO);
                    winningReportDTO.setRankDTOList(inValidRankDTOList);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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

            @Nested
            @DisplayName("Validation(Rank) 테스트")
            class Test_Update_Valid_Rank {

                @Test
                @DisplayName("실패(Validation_Ranking 예외)")
                void fail_valid_ranking() {
                    // given
                    Integer inValidRanking = -1;
                    winningReportDTO.getRankDTOList().get(0).setRanking(inValidRanking);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                @DisplayName("실패(Validation_WinningCount 예외)")
                void fail_valid_winningCount() {
                    // given
                    Integer inValidWinningCount = -1;
                    winningReportDTO.getRankDTOList().get(0).setWinningCount(inValidWinningCount);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() {
                    // given
                    Long inValidTotalWinningAmount = -1L;
                    winningReportDTO.getRankDTOList().get(0).setTotalWinningAmount(inValidTotalWinningAmount);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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
                @DisplayName("실패(Validation_WinningAmount 예외)")
                void fail_valid_winningAmount() {
                    // given
                    Long inValidWinningAmount = -1L;
                    winningReportDTO.getRankDTOList().get(0).setWinningAmount(inValidWinningAmount);

                    HttpEntity<WinningReportDTO> winningReportDTOHttpEntity = new HttpEntity<>(winningReportDTO);

                    // when
                    ResponseEntity<ErrorDTO> errorResponse =
                            testRestTemplate.exchange(
                                    "/winningReport/put/update/" + winningReportDTO.getRound(),
                                    HttpMethod.PUT,
                                    winningReportDTOHttpEntity,
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

        @Nested
        @DisplayName("delete 테스트")
        class Test_Delete {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = 1111;

                winningReportRepository.save(winningReportDTO.toEntity());

                // when
                ResponseEntity<Void> response = testRestTemplate.exchange(
                                "/winningReport/delete/" + round,
                                HttpMethod.DELETE,
                                null,
                                Void.class
                );

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

                boolean isDeleted = winningReportRepository.existsByRound(round);
                assertThat(isDeleted).isFalse();
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistRound = 1;

                // when
                ResponseEntity<ErrorDTO> errorResponse = testRestTemplate.exchange(
                        "/winningReport/delete/" + notExistRound,
                        HttpMethod.DELETE,
                        null,
                        ErrorDTO.class
                );

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

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
                        "/winningReport/delete/" + inValidRound,
                        HttpMethod.DELETE,
                        null,
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

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        winningReportRepository.deleteAll();
    }

    // winningReport assertThat 함수
    void winningReportAssertThat(ResponseEntity<WinningReportDTO> response, WinningReportDTO inputWinningReportDTO,
                                 WinningReportDTO outputWinningReportDTO, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputWinningReportDTO.getRound())
                .isEqualTo(inputWinningReportDTO.getRound());
        assertThat(outputWinningReportDTO.getDate())
                .isEqualTo(inputWinningReportDTO.getDate());
        assertThat(outputWinningReportDTO.getTotalWinningAmount())
                .isEqualTo(inputWinningReportDTO.getTotalWinningAmount());

        RankDTO inputRankDTO = inputWinningReportDTO.getRankDTOList().get(0);
        RankDTO outputRankDTO = outputWinningReportDTO.getRankDTOList().get(0);

        assertThat(outputRankDTO.getRanking())
                .isEqualTo(inputRankDTO.getRanking());
        assertThat(outputRankDTO.getWinningCount())
                .isEqualTo(inputRankDTO.getWinningCount());
        assertThat(outputRankDTO.getTotalWinningAmount())
                .isEqualTo(inputRankDTO.getTotalWinningAmount());
        assertThat(outputRankDTO.getWinningAmount())
                .isEqualTo(inputRankDTO.getWinningAmount());

    }

    // winningReportList assertThat 함수
    void winningReportListAssertThat(ResponseEntity<WinningReportDTO[]> response, WinningReportDTO inputWinningReportDTO,
                                     List<WinningReportDTO> outputWinningReportDTOList, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputWinningReportDTOList)
                .isNotEmpty();

        assertThat(outputWinningReportDTOList.get(0).getRound())
                .isEqualTo(inputWinningReportDTO.getRound());
        assertThat(outputWinningReportDTOList.get(0).getDate())
                .isEqualTo(inputWinningReportDTO.getDate());
        assertThat(outputWinningReportDTOList.get(0).getTotalWinningAmount())
                .isEqualTo(inputWinningReportDTO.getTotalWinningAmount());

        RankDTO inputRankDTO = inputWinningReportDTO.getRankDTOList().get(0);
        RankDTO outputRankDTO = outputWinningReportDTOList.get(0).getRankDTOList().get(0);

        assertThat(outputRankDTO.getRanking())
                .isEqualTo(inputRankDTO.getRanking());
        assertThat(outputRankDTO.getWinningCount())
                .isEqualTo(inputRankDTO.getWinningCount());
        assertThat(outputRankDTO.getTotalWinningAmount())
                .isEqualTo(inputRankDTO.getTotalWinningAmount());
        assertThat(outputRankDTO.getWinningAmount())
                .isEqualTo(inputRankDTO.getWinningAmount());

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

    void logger(WinningReportDTO winningReportDTO) {
        log.info("round: {}", winningReportDTO.getRound());
        log.info("date: {}", winningReportDTO.getDate());
        log.info("totalWinningAmount: {}", winningReportDTO.getTotalWinningAmount());
        log.info("rankList: {}", winningReportDTO.getRankDTOList());
    }

    void errorLogger(ErrorDTO errorDTO) {
        log.info("code: {}", errorDTO.getCode());
        log.info("detail: {}", errorDTO.getDetail());
    }

}
