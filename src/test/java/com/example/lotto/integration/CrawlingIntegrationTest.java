package com.example.lotto.integration;

import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.error.ErrorDTO;
import com.example.lotto.repository.ResultRepository;
import com.example.lotto.repository.WinningReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlingIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private WinningReportRepository winningReportRepository;

    private CrawlingModel crawlingModel;

    @BeforeEach
    @DisplayName("데이터 설정")
    void setUp() {
        crawlingModel = new CrawlingModel();

        Integer round = 1111;
        LocalDate date = LocalDate.parse("2024-03-16");
        List<Integer> numbers = Arrays.asList(3, 13, 30, 33, 43, 45);
        Integer bonusNumber = 4;
        Long totalWinningAmount = 116382835000L;
        List<Integer> rankings = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> winningCounts = Arrays.asList(16, 97, 5096, 171363, 2608760);
        List<Long> totalWinningAmounts = Arrays.asList(27434600640L, 4572433530L, 4572436960L, 8568150000L, 13043800000L);
        List<Long> winningAmounts = Arrays.asList(1714662540L, 47138490L, 897260L, 50000L, 5000L);


        crawlingModel.setRound(round);
        crawlingModel.setDate(date);
        crawlingModel.setNumbers(numbers);
        crawlingModel.setBonusNumber(bonusNumber);
        crawlingModel.setTotalWinningAmount(totalWinningAmount);
        crawlingModel.setRankings(rankings);
        crawlingModel.setWinningCounts(winningCounts);
        crawlingModel.setTotalWinningAmounts(totalWinningAmounts);
        crawlingModel.setWinningAmounts(winningAmounts);
    }

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        @Nested
        @DisplayName("GetByCrawlWebsite 테스트")
        class Test_GetByCrawlWebsite {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer page = 1111;

                // when
                ResponseEntity<CrawlingModel> response =
                        testRestTemplate.getForEntity("/ceawling/get/crawlWebsite/" + page, CrawlingModel.class);

                CrawlingModel findCrawlingModel = response.getBody();

                // then
                crawlingModelAssertThat(response, crawlingModel, findCrawlingModel, HttpStatus.OK);

                // log
                logger(crawlingModel);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistPage = 99999;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/ceawling/get/crawlWebsite/" + notExistPage, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_CRAWLING_MODEL;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }

        }

        @Nested
        @DisplayName("GetListByCrawlWebsite 테스트")
        class Test_GetListByCrawlWebsite {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer startPage = 1111;
                Integer endPage = 1112;

                // when
                ResponseEntity<CrawlingModel[]> response =
                        testRestTemplate.getForEntity("/crawling/get/crawlWebsite/list?startPage=" + startPage + "&endPage=" + endPage,
                                CrawlingModel[].class);

                List<CrawlingModel> crawlingModelList = Arrays.asList(response.getBody());

                // then
                crawlingModelListAssertThat(response, crawlingModel, crawlingModelList, HttpStatus.OK);

                // log
                logger(crawlingModelList.get(0));

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistStartPage = 1111;
                Integer notExistEndPage = 1112;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/crawling/get/crawlWebsite/list?startPage=" + notExistStartPage + "&endPage=" + notExistEndPage,
                                ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_CRAWLING_MODEL;

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
        @DisplayName("InsertByCrawl 테스트")
        class Test_InsertByCrawl {

            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when
                ResponseEntity<Void> response =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert", crawlingModel, Void.class);

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);

                ResultDTO resultDTO = resultRepository.findByRound(crawlingModel.getRound()).toDTO();
                WinningReportDTO winningReportDTO = winningReportRepository.findByRound(crawlingModel.getRound()).toDTO();

                resultAssertThat(resultDTO, crawlingModel);
                winningReportAssertThat(winningReportDTO, crawlingModel);

                // log
                resultLogger(resultDTO);
                winningReportLogger(winningReportDTO);
            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() {
                // given
                Integer inValidRound = null;
                crawlingModel.setRound(inValidRound);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert", crawlingModel, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

            @Test
            @DisplayName("실패(WinningReport 에외)")
            void fail_winningReport() {
                // given
                List<Integer> inValidRankings = new ArrayList<>();
                crawlingModel.setRankings(inValidRankings);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert", crawlingModel, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);
            }
        }

        @Nested
        @DisplayName("InsertAllByCrawl 테스트")
        class Test_InsertAllByCrawl {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);

                // when
                ResponseEntity<Void> response =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert/list", crawlingModelList, Void.class);

                // then
                assertThat(response)
                        .isNotNull();

                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.CREATED);

                ResultDTO resultDTO = resultRepository.findByRound(crawlingModelList.get(0).getRound()).toDTO();
                WinningReportDTO winningReportDTO = winningReportRepository.findByRound(crawlingModelList.get(0).getRound()).toDTO();

                resultAssertThat(resultDTO, crawlingModel);
                winningReportAssertThat(winningReportDTO, crawlingModel);

                // log
                resultLogger(resultDTO);
                winningReportLogger(winningReportDTO);

            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() {
                // given
                Integer inValidRound = null;
                crawlingModel.setRound(inValidRound);
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert/list", crawlingModelList, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

            @Test
            @DisplayName("실패(WinningReport 에외)")
            void fail_winningReport() {
                // given
                List<Integer> inValidRankings = new ArrayList<>();
                crawlingModel.setRankings(inValidRankings);
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.postForEntity("/crawlingModel/post/insert/list", crawlingModelList, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }
        }

        @Nested
        @DisplayName("InsertAll 테스트")
        class Test_InsertAll {

            @Test
            @DisplayName("성공")
            void success() {

            }

            @Test
            @DisplayName("실패")
            void fail() {

            }

        }

    }

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        resultRepository.deleteAll();
        winningReportRepository.deleteAll();
    }

    // crawlingModel assertThat 함수
    void crawlingModelAssertThat(ResponseEntity<CrawlingModel> response, CrawlingModel inputCrawlingModel,
                                 CrawlingModel outputCrawlingModel, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputCrawlingModel.getRound())
                .isEqualTo(inputCrawlingModel.getRound());
        assertThat(outputCrawlingModel.getDate())
                .isEqualTo(inputCrawlingModel.getDate());
        assertThat(outputCrawlingModel.getNumbers())
                .isEqualTo(inputCrawlingModel.getNumbers());
        assertThat(outputCrawlingModel.getBonusNumber())
                .isEqualTo(inputCrawlingModel.getBonusNumber());
        assertThat(outputCrawlingModel.getTotalWinningAmount())
                .isEqualTo(inputCrawlingModel.getTotalWinningAmount());
        assertThat(outputCrawlingModel.getRankings())
                .isEqualTo(inputCrawlingModel.getRankings());
        assertThat(outputCrawlingModel.getWinningCounts())
                .isEqualTo(inputCrawlingModel.getWinningCounts());
        assertThat(outputCrawlingModel.getTotalWinningAmounts())
                .isEqualTo(inputCrawlingModel.getTotalWinningAmounts());
        assertThat(outputCrawlingModel.getWinningAmounts())
                .isEqualTo(inputCrawlingModel.getWinningAmounts());

    }

    // crawlingModelList assertThat 함수
    void crawlingModelListAssertThat(ResponseEntity<CrawlingModel[]> response, CrawlingModel inputCrawlingModel,
                                     List<CrawlingModel> outputCrawlingModelList, HttpStatus httpStatus) {

        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputCrawlingModelList)
                .isNotEmpty();

        assertThat(outputCrawlingModelList.get(0).getRound())
                .isEqualTo(inputCrawlingModel.getRound());
        assertThat(outputCrawlingModelList.get(0).getDate())
                .isEqualTo(inputCrawlingModel.getDate());
        assertThat(outputCrawlingModelList.get(0).getNumbers())
                .isEqualTo(inputCrawlingModel.getNumbers());
        assertThat(outputCrawlingModelList.get(0).getBonusNumber())
                .isEqualTo(inputCrawlingModel.getBonusNumber());
        assertThat(outputCrawlingModelList.get(0).getTotalWinningAmount())
                .isEqualTo(inputCrawlingModel.getTotalWinningAmount());
        assertThat(outputCrawlingModelList.get(0).getRankings())
                .isEqualTo(inputCrawlingModel.getRankings());
        assertThat(outputCrawlingModelList.get(0).getWinningCounts())
                .isEqualTo(inputCrawlingModel.getWinningCounts());
        assertThat(outputCrawlingModelList.get(0).getTotalWinningAmounts())
                .isEqualTo(inputCrawlingModel.getTotalWinningAmounts());
        assertThat(outputCrawlingModelList.get(0).getWinningAmounts())
                .isEqualTo(inputCrawlingModel.getWinningAmounts());


    }

    // result assertThat 함수
    void resultAssertThat(ResultDTO resultDTO, CrawlingModel crawlingModel) {
        assertThat(resultDTO.getRound())
                .isEqualTo(crawlingModel.getRound());
        assertThat(resultDTO.getNumbers())
                .isEqualTo(crawlingModel.getNumbers());
        assertThat(resultDTO.getBonusNumber())
                .isEqualTo(crawlingModel.getBonusNumber());
        assertThat(resultDTO.getDate())
                .isEqualTo(crawlingModel.getDate());
    }

    // winningReport assertThat 함수
    void winningReportAssertThat(WinningReportDTO winningReportDTO, CrawlingModel crawlingModel) {
        assertThat(winningReportDTO.getRound())
                .isEqualTo(crawlingModel.getRound());
        assertThat(winningReportDTO.getDate())
                .isEqualTo(crawlingModel.getDate());
        assertThat(winningReportDTO.getTotalWinningAmount())
                .isEqualTo(crawlingModel.getTotalWinningAmount());

        RankDTO rankDTO = winningReportDTO.getRankDTOList().get(0);

        assertThat(rankDTO.getRanking())
                .isEqualTo(crawlingModel.getRankings());
        assertThat(rankDTO.getWinningCount())
                .isEqualTo(crawlingModel.getWinningCounts());
        assertThat(rankDTO.getTotalWinningAmount())
                .isEqualTo(crawlingModel.getTotalWinningAmounts());
        assertThat(rankDTO.getWinningAmount())
                .isEqualTo(crawlingModel.getWinningAmounts());
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
    void logger(CrawlingModel crawlingModel) {
        log.info("round: {}", crawlingModel.getRound());
        log.info("date: {}", crawlingModel.getDate());
        log.info("numbers: {}", crawlingModel.getNumbers());
        log.info("bonusNumber: {}", crawlingModel.getBonusNumber());
        log.info("totalWinningAmount: {}", crawlingModel.getTotalWinningAmount());
        log.info("rankings: {}", crawlingModel.getRankings());
        log.info("winningCounts: {}", crawlingModel.getWinningCounts());
        log.info("totalWinningAmounts: {}", crawlingModel.getTotalWinningAmounts());
        log.info("winningAmounts: {}", crawlingModel.getWinningAmounts());
    }

    // result Logger
    void resultLogger(ResultDTO resultDTO) {
        log.info("round: {}", resultDTO.getRound());
        log.info("numbers: {}", resultDTO.getNumbers());
        log.info("bonusNumber: {}", resultDTO.getBonusNumber());
        log.info("date: {}", resultDTO.getDate());
    }

    // winningReport Logger
    void winningReportLogger(WinningReportDTO winningReportDTO) {
        log.info("round: {}", winningReportDTO.getRound());
        log.info("date: {}", winningReportDTO.getDate());
        log.info("totalWinningAmount: {}", winningReportDTO.getTotalWinningAmount());
        log.info("rankList: {}", winningReportDTO.getRankDTOList());
    }

    // 에러 일때 logger
    void errorLogger(ErrorDTO errorDTO) {
        log.info("code: {}", errorDTO.getCode());
        log.info("detail: {}", errorDTO.getDetail());
    }


}
