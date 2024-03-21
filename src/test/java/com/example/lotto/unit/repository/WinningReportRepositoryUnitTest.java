package com.example.lotto.unit.repository;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.Result;
import com.example.lotto.domain.WinningReport;
import com.example.lotto.repository.WinningReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
public class WinningReportRepositoryUnitTest {

    @Autowired
    private WinningReportRepository winningReportRepository;

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
                WinningReport winningReport = winningReportRepository.findByRound(round);

                // then
                assertThat(winningReport.getRound())
                        .isEqualTo(round);

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer round = -1;

                // when
                WinningReport winningReport = winningReportRepository.findByRound(round);

                // then
                assertThat(winningReport)
                        .isNull();

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
                List<WinningReport> winningReportList = winningReportRepository.findByDateBetween(startDate, endDate);

                // then
                assertThat(winningReportList)
                        .isNotEmpty();

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("1000-03-01");
                LocalDate endDate = LocalDate.parse("1000-03-31");

                // when
                List<WinningReport> winningReportList = winningReportRepository.findByDateBetween(startDate, endDate);

                // then
                assertThat(winningReportList)
                        .isEmpty();

            }
        }

        @Nested
        @DisplayName("findByTotalWinningAmountGreaterThanEqual 테스트")
        class Test_FindByTotalWinningAmountGreaterThanEqual {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Long totalWinningAmount = 1L;

                // when
                List<WinningReport> winningReportList = winningReportRepository.findByTotalWinningAmountGreaterThanEqual(totalWinningAmount);

                // then
                assertThat(winningReportList)
                        .isNotEmpty();

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Long totalWinningAmount = 1000000000000L;

                // when
                List<WinningReport> winningReportList = winningReportRepository.findByTotalWinningAmountGreaterThanEqual(totalWinningAmount);

                // then
                assertThat(winningReportList)
                        .isEmpty();

            }

        }
    }

    @Nested
    @DisplayName("insert 테스트")
    class Test_Insert {

        private WinningReport winningReport;
        private Rank rank;

        @BeforeEach
        @DisplayName("객체 생성")
        void setUp() {
            Integer round = 1111;
            LocalDate date = LocalDate.of(2024, 3, 2);
            Long totalWinningAmount = 1L;

            rank = Rank.builder()
                    .ranking(1)
                    .winningCount(1)
                    .totalWinningAmount(1L)
                    .winningAmount(1L)
                    .build();

            List<Rank> rankList = List.of(rank);

            winningReport = WinningReport.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankList(rankList)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given

            // when
            WinningReport savedWinningReport = winningReportRepository.insert(winningReport);

            // then
            assertThat(savedWinningReport)
                    .isEqualTo(winningReport);

        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given

            // when
            WinningReport savedWinningReport = winningReportRepository.insert(winningReport);

            // then
            assertThat(savedWinningReport)
                    .isEqualTo(winningReport);

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
            WinningReport winningReport = winningReportRepository.findByRound(round);

            Integer updateRound = 1112;
            winningReport.setRound(updateRound);

            // when
            WinningReport updateWinningReport = winningReportRepository.save(winningReport);

            // then
            assertThat(updateWinningReport.getRound())
                    .isEqualTo(updateRound);

        }

        @Test
        @DisplayName("실패(없는 문서를 업데이트 시)")
        void fail_null() {
            // given
            Integer round = -1;

            // when & then
            assertThatThrownBy(() -> {
                WinningReport winningReport = winningReportRepository.findByRound(round);
                winningReport.setRound(round);
                winningReportRepository.save(winningReport);
            }).isInstanceOf(NullPointerException.class);

        }

        @Test
        @DisplayName("실패(중복 문서로 업데이트 시)")
        void fail_duplication() {
            // given
            Integer round = 1111;
            Integer updateRound = 1112;

            // when & then
            assertThatThrownBy(() -> {
                WinningReport winningReport = winningReportRepository.findByRound(round);
                winningReport.setRound(updateRound);
                winningReportRepository.save(winningReport);
            }).isInstanceOf(DuplicateKeyException.class);

        }

    }

    @Nested
    @DisplayName("delete 테스트")
    class Test_Delete {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1112;

            // when
            Integer deleteCount = winningReportRepository.deleteByRound(round);

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
            Integer deleteCount = winningReportRepository.deleteByRound(round);

            // then
            assertThat(deleteCount)
                    .isZero();

        }

    }

}
