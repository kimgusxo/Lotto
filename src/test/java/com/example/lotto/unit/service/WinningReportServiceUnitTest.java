package com.example.lotto.unit.service;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.WinningReport;
import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.WinningReportRepository;
import com.example.lotto.service.WinningReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

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
public class WinningReportServiceUnitTest {

    @Mock
    private WinningReportRepository winningReportRepository;

    @InjectMocks
    private WinningReportService winningReportService;

    @Nested
    @DisplayName("Read 테스트")
    class Test_Read {

        private WinningReport winningReport;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            LocalDate date = LocalDate.parse("2024-03-16");
            Long totalWinningAmount = 116382835000L;
            Rank rank = Rank.builder()
                    .ranking(1)
                    .winningCount(16)
                    .totalWinningAmount(27434600640L)
                    .winningAmount(1714662540L)
                    .build();

            List<Rank> rankList = new ArrayList<>(Arrays.asList(rank));

            winningReport = WinningReport.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankList(rankList)
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
                given(winningReportRepository.existsByRound(round)).willReturn(true);
                given(winningReportRepository.findByRound(round)).willReturn(winningReport);

                // when
                WinningReportDTO winningReportDTO = winningReportService.readByRound(round);

                // then
                assertThat(winningReportDTO.getRound())
                        .isEqualTo(round);

                then(winningReportRepository).should(times(1)).existsByRound(round);
                then(winningReportRepository).should(times(1)).findByRound(round);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer round = -1;
                given(winningReportRepository.existsByRound(round)).willReturn(false);

                // when & then
                assertThatThrownBy(() -> winningReportService.readByRound(round))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_WINNING_REPORT_ROUND);

                then(winningReportRepository).should(times(1)).existsByRound(round);
                then(winningReportRepository).should(times(0)).findByRound(round);
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

                List<WinningReport> winningReportList = new ArrayList<>(Arrays.asList(winningReport));

                given(winningReportRepository.findByDateBetween(startDate, endDate)).willReturn(winningReportList);

                // when
                List<WinningReportDTO> winningReportDTOList = winningReportService.readByDate(startDate, endDate);

                // then
                assertThat(winningReportDTOList)
                        .isNotEmpty();

                then(winningReportRepository).should(times(1)).findByDateBetween(startDate, endDate);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                LocalDate startDate = LocalDate.parse("2000-01-01");
                LocalDate endDate = LocalDate.parse("2000-01-31");

                List<WinningReport> winningReportList = new ArrayList<>();

                given(winningReportRepository.findByDateBetween(startDate, endDate)).willReturn(winningReportList);

                // when & then
                assertThatThrownBy(() -> winningReportService.readByDate(startDate, endDate))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INCORRECT_WINNING_REPORT_DATE);

                then(winningReportRepository).should(times(1)).findByDateBetween(startDate, endDate);
            }

        }

        @Nested
        @DisplayName("readByTotalWinningAmount 테스트")
        class Test_ReadByTotalWinningAmount {

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Long totalWinningAmount = 1000000L;

                List<WinningReport> winningReportList = new ArrayList<>(Arrays.asList(winningReport));

                given(winningReportRepository.findByTotalWinningAmountGreaterThanEqual(totalWinningAmount)).willReturn(winningReportList);

                // when
                List<WinningReportDTO> winningReportDTOList = winningReportService.readByTotalWinningAmount(totalWinningAmount);

                // then
                assertThat(winningReportDTOList)
                        .isNotEmpty();

                then(winningReportRepository).should(times(1)).findByTotalWinningAmountGreaterThanEqual(totalWinningAmount);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Long totalWinningAmount = 999999999999999L;

                List<WinningReport> winningReportList = new ArrayList<>();

                given(winningReportRepository.findByTotalWinningAmountGreaterThanEqual(totalWinningAmount)).willReturn(winningReportList);

                // when & then
                assertThatThrownBy(() -> winningReportService.readByTotalWinningAmount(totalWinningAmount))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_WINNING_REPORT);

                then(winningReportRepository).should(times(1)).findByTotalWinningAmountGreaterThanEqual(totalWinningAmount);
            }

        }

    }

    @Nested
    @DisplayName("Insert 테스트")
    class Test_Insert {

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

            List<RankDTO> rankDTOList = new ArrayList<>(Arrays.asList(rankDTO));

            winningReportDTO = WinningReportDTO.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankDTOList(rankDTOList)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            WinningReport winningReport = winningReportDTO.toEntity();

            given(winningReportRepository.existsByRound(winningReport.getRound())).willReturn(false);
            given(winningReportRepository.insert(winningReport)).willReturn(winningReport);

            // when
            WinningReportDTO savedWinningReportDTO = winningReportService.insert(winningReportDTO);

            // then
            assertThat(savedWinningReportDTO)
                    .isEqualTo(winningReportDTO);

            then(winningReportRepository).should(times(1)).existsByRound(winningReport.getRound());
            then(winningReportRepository).should(times(1)).insert(winningReport);
        }

        @Test
        @DisplayName("실패(Repository 예외)")
        void fail_duplication() {
            // given
            WinningReport winningReport = winningReportDTO.toEntity();

            given(winningReportRepository.existsByRound(winningReport.getRound())).willReturn(false);
            given(winningReportRepository.insert(winningReport)).willThrow(DuplicateKeyException.class);

            // when & then
            assertThatThrownBy(() -> winningReportService.insert(winningReportDTO))
                    .isInstanceOf(DuplicateKeyException.class);

            then(winningReportRepository).should(times(1)).existsByRound(winningReport.getRound());
            then(winningReportRepository).should(times(1)).insert(winningReport);
        }

        @Test
        @DisplayName("실패(Service 예외)")
        void fail_null() {
            // given
            WinningReport winningReport = winningReportDTO.toEntity();

            given(winningReportRepository.existsByRound(winningReport.getRound())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> winningReportService.insert(winningReportDTO))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_WINNING_REPORT_ROUND);

            then(winningReportRepository).should(times(1)).existsByRound(winningReport.getRound());
            then(winningReportRepository).should(times(0)).insert(winningReport);
        }
    }

    @Nested
    @DisplayName("Update 테스트")
    class Test_Update {

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

            List<RankDTO> rankDTOList = new ArrayList<>(Arrays.asList(rankDTO));

            winningReportDTO = WinningReportDTO.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankDTOList(rankDTOList)
                    .build();
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer updateRound = 1112;

            WinningReport winningReport = winningReportDTO.toEntity();
            winningReport.setRound(updateRound);

            given(winningReportRepository.findByRound(winningReport.getRound())).willReturn(winningReport);
            given(winningReportRepository.save(winningReport)).willReturn(winningReport);

            // when
            WinningReportDTO updateWinningReportDTO = winningReportService.update(updateRound, winningReportDTO);

            // then
            assertThat(updateWinningReportDTO.getRound())
                    .isEqualTo(updateRound);

            then(winningReportRepository).should(times(1)).findByRound(winningReport.getRound());
            then(winningReportRepository).should(times(1)).save(winningReport);
        }

        @Test
        @DisplayName("실패(Repository 예외)")
        void fail_duplication() {
            // given
            Integer updateRound = 1112;

            WinningReport winningReport = winningReportDTO.toEntity();
            winningReport.setRound(updateRound);

            given(winningReportRepository.findByRound(winningReport.getRound())).willReturn(winningReport);
            given(winningReportRepository.save(winningReport)).willThrow(DuplicateKeyException.class);

            // when & then
            assertThatThrownBy(() -> winningReportService.update(updateRound, winningReportDTO))
                    .isInstanceOf(DuplicateKeyException.class);

            then(winningReportRepository).should(times(1)).findByRound(winningReport.getRound());
            then(winningReportRepository).should(times(1)).save(winningReport);
        }

        @Test
        @DisplayName("실패(Repository 예외)")
        void fail_null() {
            // given
            Integer updateRound = 1112;

            WinningReport winningReport = winningReportDTO.toEntity();
            winningReport.setRound(updateRound);

            given(winningReportRepository.findByRound(winningReport.getRound())).willReturn(null);

            // when
            assertThatThrownBy(() -> winningReportService.update(updateRound, winningReportDTO))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_WINNING_REPORT);

            // then
            then(winningReportRepository).should(times(1)).findByRound(winningReport.getRound());
            then(winningReportRepository).should(times(0)).save(winningReport);

        }

    }

    @Nested
    @DisplayName("Delete 테스트")
    class Test_Delete {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Integer round = 1111;

            given(winningReportRepository.deleteByRound(round)).willReturn(1);

            // when
            winningReportService.delete(round);

            // then
            then(winningReportRepository).should(times(1)).deleteByRound(round);

        }

        @Test
        @DisplayName("실패")
        void fail() {
            // given
            Integer round = 1111;

            given(winningReportRepository.deleteByRound(round)).willReturn(0);

            // when & then
            assertThatThrownBy(() -> winningReportService.delete(round))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

    }

}
