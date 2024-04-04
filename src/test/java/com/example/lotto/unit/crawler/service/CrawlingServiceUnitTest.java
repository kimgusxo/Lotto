package com.example.lotto.unit.crawler.service;

import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.crawler.service.CrawlingService;
import com.example.lotto.repository.ResultRepository;
import com.example.lotto.repository.WinningReportRepository;
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

@ExtendWith(MockitoExtension.class)
public class CrawlingServiceUnitTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private WinningReportRepository winningReportRepository;

    @InjectMocks
    private CrawlingService crawlingService;

    private CrawlingModel crawlingModel;

    @BeforeEach
    @DisplayName("데이터 설정")
    void setUp() {
        crawlingModel = new CrawlingModel();

        Integer round = 1111;
        LocalDate date = LocalDate.parse("2024-03-16");
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Integer bonusNumber = 4;
        Long totalWinningAmount = 1111L;
        List<Integer> rankings = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> winningCounts = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Long> totalWinningAmounts = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        List<Long> winningAmounts = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));


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
            void success() throws IOException {
                // given

                // when

                // then
            }

            @Test
            @DisplayName("실패")
            void fail() throws IOException {
                // given

                // when

                // then
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

        @Nested
        @DisplayName("BindingWinningReport 테스트")
        class Test_BindingWinningReport {
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
    @DisplayName("Insert 테스트")
    class Test_Insert {

        @Nested
        @DisplayName("InsertByCrawl")
        class Test_InsertByCrawl {
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

        @Nested
        @DisplayName("InsertAllByCrawl")
        class Test_InsertAllByCrawl {
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

}
