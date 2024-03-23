package com.example.lotto.unit.repository;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.Result;
import com.example.lotto.domain.WinningReport;
import com.example.lotto.repository.WinningReportRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
public class WinningReportRepositoryUnitTest {

    @Autowired
    private WinningReportRepository winningReportRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Nested
    @DisplayName("find 테스트")
    class Test_Find {

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            LocalDate date = LocalDate.parse("2024-03-16");
            Long totalWinningAmount = 10000L;

            Rank rank = Rank.builder()
                    .ranking(1)
                    .winningCount(1)
                    .totalWinningAmount(1L)
                    .winningAmount(1L)
                    .build();

            List<Rank> rankList = List.of(rank);

            Optional<WinningReport> existingWinningReport = Optional.ofNullable(winningReportRepository.findByRound(round));

            if(existingWinningReport.isEmpty()) {
                WinningReport winningReport = WinningReport.builder()
                        .round(round)
                        .date(date)
                        .totalWinningAmount(totalWinningAmount)
                        .rankList(rankList)
                        .build();

                winningReportRepository.save(winningReport);
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
            LocalDate date = LocalDate.parse("2024-03-16");
            Long totalWinningAmount = 10000L;

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
            winningReportRepository.insert(winningReport);

            // when & then
            assertThatThrownBy(() -> winningReportRepository.insert(winningReport))
                    .isInstanceOf(DuplicateKeyException.class);
        }

    }

    @Nested
    @DisplayName("update 테스트")
    class Test_Update {

        @BeforeEach
        @DisplayName("데이터 생성")
        void setUp() {
            IndexOperations indexOps = mongoTemplate.indexOps(WinningReport.class);
            indexOps.ensureIndex(new Index().on("round", Sort.Direction.ASC).unique());

            Integer round1 = 1111;
            Integer round2 = 1112;

            LocalDate date = LocalDate.parse("2024-03-16");
            Long totalWinningAmount = 10000L;

            Rank rank = Rank.builder()
                    .ranking(1)
                    .winningCount(1)
                    .totalWinningAmount(1L)
                    .winningAmount(1L)
                    .build();

            List<Rank> rankList = List.of(rank);

            Optional<WinningReport> existingWinningReport1 = Optional.ofNullable(winningReportRepository.findByRound(round1));

            if(existingWinningReport1.isEmpty()) {
                WinningReport winningReport1 = WinningReport.builder()
                        .round(round1)
                        .date(date)
                        .totalWinningAmount(totalWinningAmount)
                        .rankList(rankList)
                        .build();

                winningReportRepository.save(winningReport1);
            }

            Optional<WinningReport> existingWinningReport2 = Optional.ofNullable(winningReportRepository.findByRound(round2));

            if(existingWinningReport2.isEmpty()) {
                WinningReport winningReport2 = WinningReport.builder()
                        .round(round2)
                        .date(date)
                        .totalWinningAmount(totalWinningAmount)
                        .rankList(rankList)
                        .build();

                winningReportRepository.save(winningReport2);
            }

        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;
            WinningReport winningReport = winningReportRepository.findByRound(round);

            Integer updateRound = 1113;
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

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            LocalDate date = LocalDate.parse("2024-03-16");
            Long totalWinningAmount = 10000L;

            Rank rank = Rank.builder()
                    .ranking(1)
                    .winningCount(1)
                    .totalWinningAmount(1L)
                    .winningAmount(1L)
                    .build();

            List<Rank> rankList = List.of(rank);

            Optional<WinningReport> existingWinningReport = Optional.ofNullable(winningReportRepository.findByRound(round));

            if(existingWinningReport.isEmpty()) {
                WinningReport winningReport = WinningReport.builder()
                        .round(round)
                        .date(date)
                        .totalWinningAmount(totalWinningAmount)
                        .rankList(rankList)
                        .build();

                winningReportRepository.save(winningReport);
            }
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;

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

    @AfterEach
    @DisplayName("데이터 정리")
    void clear() {
        mongoTemplate.dropCollection("winning_report");
        mongoTemplate.dropCollection("rank");
    }

}
