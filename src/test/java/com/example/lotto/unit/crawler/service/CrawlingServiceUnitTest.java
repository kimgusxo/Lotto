package com.example.lotto.unit.crawler.service;

import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.crawler.service.CrawlingService;
import com.example.lotto.crawler.utils.CrawlingUtils;
import com.example.lotto.domain.Rank;
import com.example.lotto.domain.Result;
import com.example.lotto.domain.WinningReport;
import com.example.lotto.error.CustomException;
import com.example.lotto.repository.ResultRepository;
import com.example.lotto.repository.WinningReportRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CrawlingServiceUnitTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private WinningReportRepository winningReportRepository;

    @Mock
    private CrawlingUtils crawlingUtils;

    @InjectMocks
    private CrawlingService crawlingService;

    private CrawlingModel crawlingModel;

    @BeforeEach
    @DisplayName("데이터 설정")
    void setUp() {
        crawlingModel = new CrawlingModel();

        Integer round = 1111;
        LocalDate date = LocalDate.parse("2024-03-16");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Integer bonusNumber = 4;
        Long totalWinningAmount = 1111L;
        List<Integer> rankings = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> winningCounts = Arrays.asList(1, 2, 3, 4, 5);
        List<Long> totalWinningAmounts = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<Long> winningAmounts = Arrays.asList(1L, 2L, 3L, 4L, 5L);


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
    @DisplayName("Crawling 테스트")
    class Test_Crawling {

        @Nested
        @DisplayName("CrawlWebsite 테스트")
        class Test_CrawlWebSite {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer page = 1111;
                String url = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + page;
                String html = "<html>" +
                        "<head>" +
                        "    <title>로또 당첨 결과</title>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"win_result\">" +
                        "        <h4><strong>1111회</strong></h4>" +
                        "        <p class=\"desc\">(2024년 03월 16일 추첨)</p>" +
                        "    </div>" +
                        "    <div class=\"num win\">" +
                        "        <p><span>1</span> <span>2</span> <span>3</span> <span>4</span> <span>5</span> <span>6</span></p>" +
                        "    </div>" +
                        "    <div class=\"num bonus\">" +
                        "        <p><span>4</span></p>" +
                        "    </div>" +
                        "    <ul class=\"list_text_common\">" +
                        "        <li><strong>1111</strong></li>" +
                        "    </ul>" +
                        "    <table>" +
                        "    <tbody>" +
                        "        <tr><td>1등</td><td>1,111</td><td>1</td><td>1</td></tr>" +
                        "        <tr><td>2등</td><td>2,222</td><td>2</td><td>2</td></tr>" +
                        "        <tr><td>3등</td><td>3,333</td><td>3</td><td>3</td></tr>" +
                        "        <tr><td>4등</td><td>4,444</td><td>4</td><td>4</td></tr>" +
                        "        <tr><td>5등</td><td>5,555</td><td>5</td><td>5</td></tr>" +
                        "    </tbody>" +
                        "    </table>" +
                        "</body>" +
                        "</html>";

                given(crawlingUtils.getDocument(url)).willReturn(Jsoup.parse(html));

                // when
                crawlingModel = crawlingService.crawlWebsite(page);

                // then
                assertThat(crawlingModel.getRound())
                        .isEqualTo(page);
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                Integer page = 1111;
                String url = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + page;

                given(crawlingUtils.getDocument(url)).willThrow(IOException.class);

                // when & then
                assertThatThrownBy(() -> crawlingService.crawlWebsite(page))
                        .isInstanceOf(IOException.class);
            }

        }

    }

    @Nested
    @DisplayName("Binding 테스트")
    class Test_Binding {

        @Nested
        @DisplayName("BindingResult 테스트")
        class Test_BindingResult {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = crawlingModel.getRound();

                // when
                Result result = crawlingService.bindingResult(crawlingModel);

                // then
                assertThat(result.getRound())
                        .isEqualTo(round);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // Given
                CrawlingModel model = new CrawlingModel();

                // When & Then
                assertThatThrownBy(() -> crawlingService.bindingResult(model))
                        .isInstanceOf(CustomException.class);
            }
        }

        @Nested
        @DisplayName("BindingWinningReport 테스트")
        class Test_BindingWinningReport {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer round = crawlingModel.getRound();

                // when
                WinningReport winningReport = crawlingService.bindingWinningReport(crawlingModel);

                // then
                assertThat(winningReport.getRound())
                        .isEqualTo(round);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                crawlingModel.setDate(null);

                // when & then
                assertThatThrownBy(() -> crawlingService.bindingWinningReport(crawlingModel))
                        .isInstanceOf(CustomException.class);
            }
        }

    }

    @Nested
    @DisplayName("Insert 테스트")
    class Test_Insert {

        private Result result;
        private WinningReport winningReport;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            result = Result.builder()
                    .round(crawlingModel.getRound())
                    .numbers(crawlingModel.getNumbers())
                    .bonusNumber(crawlingModel.getBonusNumber())
                    .date(crawlingModel.getDate())
                    .build();

            Rank rank = Rank.builder()
                    .ranking(crawlingModel.getRankings().get(0))
                    .winningCount(crawlingModel.getWinningCounts().get(0))
                    .totalWinningAmount(crawlingModel.getTotalWinningAmounts().get(0))
                    .winningAmount(crawlingModel.getWinningAmounts().get(0))
                    .build();

            List<Rank> rankList = Arrays.asList(rank);

            winningReport = WinningReport.builder()
                    .round(crawlingModel.getRound())
                    .date(crawlingModel.getDate())
                    .totalWinningAmount(crawlingModel.getTotalWinningAmount())
                    .rankList(rankList)
                    .build();
        }

        @Nested
        @DisplayName("InsertByCrawl")
        class Test_InsertByCrawl {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                given(resultRepository.save(any())).willReturn(result);
                given(winningReportRepository.save(any())).willReturn(winningReport);

                // when
                crawlingService.insertByCrawl(crawlingModel);

                // then
                then(resultRepository).should(times(1)).save(any());
                then(winningReportRepository).should(times(1)).save(any());
            }

            @Test
            @DisplayName("실패(Result)")
            void fail_result() {
                // given
                given(resultRepository.save(any())).willThrow(IllegalArgumentException.class);

                // when & then
                assertThatThrownBy(() -> crawlingService.insertByCrawl(crawlingModel))
                        .isInstanceOf(IllegalArgumentException.class);

                then(resultRepository).should(times(1)).save(result);
                then(winningReportRepository).should(times(0)).save(winningReport);
            }

            @Test
            @DisplayName("실패(WinningReport)")
            void fail_winningReport() {
                // given
                given(resultRepository.save(any())).willReturn(result);
                given(winningReportRepository.save(any())).willThrow(IllegalArgumentException.class);

                // when & then
                assertThatThrownBy(() -> crawlingService.insertByCrawl(crawlingModel))
                        .isInstanceOf(IllegalArgumentException.class);
                then(resultRepository).should(times(1)).save(any());
                then(winningReportRepository).should(times(1)).save(any());
            }
        }

        @Nested
        @DisplayName("InsertAllByCrawl")
        class Test_InsertAllByCrawl {
            @Test
            @DisplayName("성공")
            void success() {
                // given
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);
                List<Result> resultList = Arrays.asList(result);
                List<WinningReport> winningReportList = Arrays.asList(winningReport);

                given(resultRepository.saveAll(anyList())).willReturn(resultList);
                given(winningReportRepository.saveAll(anyList())).willReturn(winningReportList);

                // when
                crawlingService.insertAllByCrawl(crawlingModelList);

                // then
                then(resultRepository).should(times(1)).saveAll(anyList());
                then(winningReportRepository).should(times(1)).saveAll(anyList());
            }

            @Test
            @DisplayName("실패(ResultList)")
            void fail_resultList() {
                // given
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);

                given(resultRepository.saveAll(anyList())).willThrow(IllegalArgumentException.class);

                // when & then
                assertThatThrownBy(() -> crawlingService.insertAllByCrawl(crawlingModelList))
                        .isInstanceOf(IllegalArgumentException.class);

                then(resultRepository).should(times(1)).saveAll(anyList());
            }

            @Test
            @DisplayName("실패(ResultList)")
            void fail_winningReportList() {
                // given
                List<CrawlingModel> crawlingModelList = Arrays.asList(crawlingModel);
                List<Result> resultList = Arrays.asList(result);

                given(resultRepository.saveAll(anyList())).willReturn(resultList);
                given(winningReportRepository.saveAll(anyList())).willThrow(IllegalArgumentException.class);

                // when & then
                assertThatThrownBy(() -> crawlingService.insertAllByCrawl(crawlingModelList))
                        .isInstanceOf(IllegalArgumentException.class);

                then(resultRepository).should(times(1)).saveAll(anyList());
                then(winningReportRepository).should(times(1)).saveAll(anyList());
            }
        }
    }

}
