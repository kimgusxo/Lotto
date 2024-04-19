package com.example.lotto.unit.controller;

import com.example.lotto.controller.WinningReportController;
import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.service.WinningReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WinningReportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WinningReportControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WinningReportService winningReportService;

    @Nested
    @DisplayName("GET 테스트")
    class Test_GET {
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

            List<RankDTO> rankDTOList = Arrays.asList(rankDTO);

            winningReportDTO = WinningReportDTO.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankDTOList(rankDTOList)
                    .build();
        }

        @Nested
        @DisplayName("getByRound 테스트")
        class Test_GetByRound {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer round = 1111;

                given(winningReportService.readByRound(round)).willReturn(winningReportDTO);

                // when & then
                mvc.perform(get("/winningReport/get/round/" + round)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.round").value(round))
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer round = 1;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT_ROUND;

                given(winningReportService.readByRound(round)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/winningReport/get/round/" + round)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() throws Exception {
                // given
                Integer inValidRound = -1;

                // when & then
                mvc.perform(get("/winningReport/get/round/" + inValidRound))
                        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                        .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                        .andExpect(status().isBadRequest());

            }

        }

        @Nested
        @DisplayName("getByDate 테스트")
        class Test_GetByDate {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");

                List<WinningReportDTO> winningReportDTOList = Arrays.asList(winningReportDTO);

                given(winningReportService.readByDate(startDate, endDate)).willReturn(winningReportDTOList);

                // when & then
                mvc.perform(get("/winningReport/get/date?startDate=" + startDate + "&endDate=" + endDate)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].date").value("2024-03-16"))
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                LocalDate startDate = LocalDate.parse("2024-03-01");
                LocalDate endDate = LocalDate.parse("2024-03-31");
                ErrorCode errorCode = ErrorCode.INCORRECT_WINNING_REPORT_DATE;

                List<WinningReportDTO> winningReportDTOList = new ArrayList<>();

                given(winningReportService.readByDate(startDate, endDate)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/winningReport/get/date?startDate=" + startDate + "&endDate=" + endDate)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() throws Exception {
                // given
                LocalDate inValidStartDate = LocalDate.parse("3000-03-01");
                LocalDate inValidEndDate = LocalDate.parse("3000-03-31");

                // when & then
                mvc.perform(get("/winningReport/get/date?startDate=" + inValidStartDate + "&endDate=" + inValidEndDate))
                        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                        .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                        .andExpect(status().isBadRequest());

            }

        }

        @Nested
        @DisplayName("getByTotalWinningAmount 테스트")
        class Test_GetByTotalWinningAmount {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Long totalWinningAmount = 100000000L;

                List<WinningReportDTO> winningReportDTOList = Arrays.asList(winningReportDTO);

                given(winningReportService.readByTotalWinningAmount(totalWinningAmount)).willReturn(winningReportDTOList);

                // when & then
                mvc.perform(get("/winningReport/get/totalWinningAmount/" + totalWinningAmount)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].totalWinningAmount").value(116382835000L))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Long totalWinningAmount = 999999999999999L;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                List<WinningReportDTO> winningReportDTOList = new ArrayList<>();

                given(winningReportService.readByTotalWinningAmount(totalWinningAmount)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/winningReport/get/totalWinningAmount/" + totalWinningAmount)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

            @Test
            @DisplayName("실패(Validation 예외)")
            void fail_valid() throws Exception {
                // given
                Long inValidTotalWinningAmount = -1000L;

                // when & then
                mvc.perform(get("/winningReport/get/totalWinningAmount/" + inValidTotalWinningAmount))
                        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                        .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                        .andExpect(status().isBadRequest());

            }

        }
    }

    @Nested
    @DisplayName("POST 테스트")
    class Test_POST {

        private WinningReportDTO winningReportDTO;
        private RankDTO rankDTO;
        private ObjectMapper objectMapper;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

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
        @DisplayName("Insert 테스트")
        class Test_Insert {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                given(winningReportService.insert(winningReportDTO)).willReturn(winningReportDTO);

                String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                // when & then
                mvc.perform(post("/winningReport/post/insert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(winningReportDTOJson))
                        .andExpect(jsonPath("$.round").value(winningReportDTO.getRound()))
                        .andExpect(jsonPath("$.date").value(winningReportDTO.getDate().toString()))
                        .andExpect(jsonPath("$.totalWinningAmount").value(winningReportDTO.getTotalWinningAmount()))
                        .andExpect(jsonPath("$.rankDTOList[0].ranking").value(winningReportDTO.getRankDTOList().get(0).getRanking()))
                        .andExpect(jsonPath("$.rankDTOList[0].winningCount").value(winningReportDTO.getRankDTOList().get(0).getWinningCount()))
                        .andExpect(jsonPath("$.rankDTOList[0].totalWinningAmount").value(winningReportDTO.getRankDTOList().get(0).getTotalWinningAmount()))
                        .andExpect(jsonPath("$.rankDTOList[0].winningAmount").value(winningReportDTO.getRankDTOList().get(0).getWinningAmount()))
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.DUPLICATE_WINNING_REPORT_ROUND;
                given(winningReportService.insert(winningReportDTO)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                // when & then
                mvc.perform(post("/winningReport/post/insert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(winningReportDTOJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

            @Nested
            @DisplayName("Validation(WinningReport) 테스트")
            class Test_Insert_Valid_WinningReport {

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_round() throws Exception {
                    // given
                    Integer inValidRound = -1;
                    winningReportDTO.setRound(inValidRound);

                    String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                    // when & then
                    mvc.perform(post("/winningReport/post/insert")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_Date 예외)")
                void fail_valid_date() throws Exception {
                    // given
                    LocalDate inValidDate = LocalDate.parse("3000-03-01");
                    winningReportDTO.setDate(inValidDate);

                    String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                    // when & then
                    mvc.perform(post("/winningReport/post/insert")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() throws Exception {
                    // given
                    Long inValidTotalWinningAmount = -1000L;
                    winningReportDTO.setTotalWinningAmount(inValidTotalWinningAmount);

                    String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                    // when & then
                    mvc.perform(post("/winningReport/post/insert")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_RankList 예외)")
                void fail_valid_rankList() throws Exception {
                    // given
                    List<RankDTO> inValidRankList = Arrays.asList(rankDTO);
                    winningReportDTO.setRankDTOList(inValidRankList);

                    String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                    // when & then
                    mvc.perform(post("/winningReport/post/insert")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());
                }

                @Nested
                @DisplayName("Validation(Rank) 테스트")
                class Test_Insert_Valid_Rank {

                    @Test
                    @DisplayName("실패(Validation_Ranking 예외)")
                    void fail_valid_ranking() throws Exception {
                        // given
                        Integer inValidRanking = -1;
                        winningReportDTO.getRankDTOList().get(0).setRanking(inValidRanking);

                        String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                        // when & then
                        mvc.perform(post("/winningReport/post/insert")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }

                    @Test
                    @DisplayName("실패(Validation_WinningCount 예외)")
                    void fail_valid_winningCount() throws Exception {
                        // given
                        Integer inValidWinningCount = -1;
                        winningReportDTO.getRankDTOList().get(0).setWinningCount(inValidWinningCount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                        // when & then
                        mvc.perform(post("/winningReport/post/insert")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());

                    }

                    @Test
                    @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                    void fail_valid_totalWinningAmount() throws Exception {
                        // given
                        Long inValidTotalWinningAmount = -1L;
                        winningReportDTO.getRankDTOList().get(0).setTotalWinningAmount(inValidTotalWinningAmount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                        // when & then
                        mvc.perform(post("/winningReport/post/insert")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }

                    @Test
                    @DisplayName("실패(Validation_WinningAmount 예외)")
                    void fail_valid_winningAmount() throws Exception {
                        // given
                        Long inValidWinningAmount = -1L;
                        winningReportDTO.getRankDTOList().get(0).setWinningAmount(inValidWinningAmount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(winningReportDTO);

                        // when & then
                        mvc.perform(post("/winningReport/post/insert")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("PUT 테스트")
    class Test_PUT {

        private WinningReportDTO updateWinningReportDTO;
        private RankDTO rankDTO;
        private ObjectMapper objectMapper;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

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

            updateWinningReportDTO = WinningReportDTO.builder()
                    .round(round)
                    .date(date)
                    .totalWinningAmount(totalWinningAmount)
                    .rankDTOList(rankDTOList)
                    .build();
        }

        @Nested
        @DisplayName("Update 테스트")
        class Test_Update {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                given(winningReportService.update(updateWinningReportDTO.getRound(), updateWinningReportDTO)).willReturn(updateWinningReportDTO);

                String updateWinningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                // when & then
                mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateWinningReportDTOJson))
                        .andExpect(jsonPath("$.round").value(updateWinningReportDTO.getRound()))
                        .andExpect(jsonPath("$.date").value(updateWinningReportDTO.getDate().toString()))
                        .andExpect(jsonPath("$.totalWinningAmount").value(updateWinningReportDTO.getTotalWinningAmount()))
                        .andExpect(jsonPath("$.rankDTOList[0].ranking").value(updateWinningReportDTO.getRankDTOList().get(0).getRanking()))
                        .andExpect(jsonPath("$.rankDTOList[0].winningCount").value(updateWinningReportDTO.getRankDTOList().get(0).getWinningCount()))
                        .andExpect(jsonPath("$.rankDTOList[0].totalWinningAmount").value(updateWinningReportDTO.getRankDTOList().get(0).getTotalWinningAmount()))
                        .andExpect(jsonPath("$.rankDTOList[0].winningAmount").value(updateWinningReportDTO.getRankDTOList().get(0).getWinningAmount()))
                        .andExpect(status().isAccepted());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                given(winningReportService.update(updateWinningReportDTO.getRound(), updateWinningReportDTO)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                String updateWinningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                // when & then
                mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateWinningReportDTOJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

            @Nested
            @DisplayName("Validation(WinningReport) 테스트")
            class Test_Update_Valid_WinningReport {

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_updateRound() throws Exception {
                    // given
                    Integer inValidUpdateRound = -1;

                    String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                    // when & then
                    mvc.perform(put("/winningReport/put/update/" + inValidUpdateRound)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_Round 예외)")
                void fail_valid_round() throws Exception {
                    // given
                    Integer validRound = 1111;
                    Integer inValidRound = -1;
                    updateWinningReportDTO.setRound(inValidRound);

                    String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                    // when & then
                    mvc.perform(put("/winningReport/put/update/" + validRound)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_Date 예외)")
                void fail_valid_date() throws Exception {
                    // given
                    LocalDate inValidDate = LocalDate.parse("3000-03-01");
                    updateWinningReportDTO.setDate(inValidDate);

                    String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                    // when & then
                    mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                void fail_valid_totalWinningAmount() throws Exception {
                    // given
                    Long inValidTotalWinningAmount = -1000L;
                    updateWinningReportDTO.setTotalWinningAmount(inValidTotalWinningAmount);

                    String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                    // when & then
                    mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());

                }

                @Test
                @DisplayName("실패(Validation_RankList 예외)")
                void fail_valid_rankList() throws Exception {
                    // given
                    List<RankDTO> inValidRankList = new ArrayList<>(Arrays.asList(rankDTO));
                    updateWinningReportDTO.setRankDTOList(inValidRankList);

                    String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                    // when & then
                    mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(winningReportDTOJson))
                            .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                            .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                            .andExpect(status().isBadRequest());
                }

                @Nested
                @DisplayName("Validation(Rank) 테스트")
                class Test_Update_Valid_Rank {

                    @Test
                    @DisplayName("실패(Validation_Ranking 예외)")
                    void fail_valid_ranking() throws Exception {
                        // given
                        Integer inValidRanking = -1;
                        updateWinningReportDTO.getRankDTOList().get(0).setRanking(inValidRanking);

                        String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                        // when & then
                        mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }

                    @Test
                    @DisplayName("실패(Validation_WinningCount 예외)")
                    void fail_valid_winningCount() throws Exception {
                        // given
                        Integer inValidWinningCount = -1;
                        updateWinningReportDTO.getRankDTOList().get(0).setWinningCount(inValidWinningCount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                        // when & then
                        mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());

                    }

                    @Test
                    @DisplayName("실패(Validation_TotalWinningAmount 예외)")
                    void fail_valid_totalWinningAmount() throws Exception {
                        // given
                        Long inValidTotalWinningAmount = -1L;
                        updateWinningReportDTO.getRankDTOList().get(0).setTotalWinningAmount(inValidTotalWinningAmount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                        // when & then
                        mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }

                    @Test
                    @DisplayName("실패(Validation_WinningAmount 예외)")
                    void fail_valid_winningAmount() throws Exception {
                        // given
                        Long inValidWinningAmount = -1L;
                        updateWinningReportDTO.getRankDTOList().get(0).setWinningAmount(inValidWinningAmount);

                        String winningReportDTOJson = objectMapper.writeValueAsString(updateWinningReportDTO);

                        // when & then
                        mvc.perform(put("/winningReport/put/update/" + updateWinningReportDTO.getRound())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(winningReportDTOJson))
                                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                                .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                                .andExpect(status().isBadRequest());
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("DELETE 테스트")
    class Test_DELETE {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            Integer round = 1111;
            willDoNothing().given(winningReportService).delete(round);

            // when & then
            mvc.perform(delete("/winningReport/delete/" + round)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            then(winningReportService).should(times(1)).delete(round);
        }

        @Test
        @DisplayName("실패")
        void fail() throws Exception {
            // given
            Integer round = 1111;
            ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

            willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(winningReportService).delete(round);

            // when & then
            mvc.perform(delete("/winningReport/delete/" + round)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            then(winningReportService).should(times(1)).delete(round);

        }

        @Test
        @DisplayName("실패(Validation 예외)")
        void fail_valid() throws Exception {
            // given
            Integer inValidRound = -1;

            // when & then
            mvc.perform(delete("/winningReport/delete/" + inValidRound))
                    .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION.getCode()))
                    .andExpect(jsonPath("$.detail").value(ErrorCode.VALIDATION.getDetail()))
                    .andExpect(status().isBadRequest());
        }

    }

}
