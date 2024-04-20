package com.example.lotto.integration;

import com.example.lotto.crawler.model.CrawlingModel;
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
import java.util.Arrays;
import java.util.List;

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

                // when

                // then

            }

        }

        @Nested
        @DisplayName("GetListByCrawlWebsite 테스트")
        class Test_GetListByCrawlWebsite {

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

                // then

            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패")
            void fail_winningReport() {
                // given

                // when

                // then

            }
        }

        @Nested
        @DisplayName("InsertAllByCrawl 테스트")
        class Test_InsertAllByCrawl {
            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패")
            void fail_winningReport() {
                // given

                // when

                // then

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
    void crawlingModelAssertThat() {

    }


}
