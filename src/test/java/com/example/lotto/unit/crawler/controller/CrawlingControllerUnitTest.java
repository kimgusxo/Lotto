package com.example.lotto.unit.crawler.controller;

import com.example.lotto.crawler.controller.CrawlingController;
import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.crawler.service.CrawlingService;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrawlingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CrawlingControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
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
    @DisplayName("GET 테스트")
    class Test_GET {

        @Nested
        @DisplayName("GetByCrawlWebsite 테스트")
        class Test_GetByCrawlWebsite {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer page = 1111;
                given(crawlingService.crawlWebsite(page)).willReturn(crawlingModel);

                // when & then
                mvc.perform(get("/crawling/get/crawlWebsite/" + page)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.round").value(page))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer page = 9999;
                ErrorCode errorCode = ErrorCode.UNKNOWN;

                given(crawlingService.crawlWebsite(page)).willThrow(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode));

                // when & then
                mvc.perform(get("/crawling/get/crawlWebsite/" + page)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isInternalServerError());
            }

        }

        @Nested
        @DisplayName("GetListByCrawlWebsite")
        class Test_GetListByCrawlWebsite {
            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer startPage = 1111;
                Integer endPage = 1112;

                given(crawlingService.crawlWebsite(startPage)).willReturn(crawlingModel);


                // when & then
                mvc.perform(get("/crawling/get/crawlWebsite/list?startPage=" + startPage + "&endPage=" + endPage)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].round").value(startPage))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer startPage = 1;
                Integer endPage = 100;

                ErrorCode errorCode = ErrorCode.UNKNOWN;

                given(crawlingService.crawlWebsite(startPage)).willThrow(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode));

                // when & then
                mvc.perform(get("/crawling/get/crawlWebsite/list?startPage=" + startPage + "&endPage=" + endPage)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isInternalServerError());
            }

        }
    }


    @Nested
    @DisplayName("POST 테스트")
    class Test_POST {

        private ObjectMapper objectMapper;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }

        @Nested
        @DisplayName("InsertByCrawl 테스트")
        class Test_InsertByCrawl {
            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                willDoNothing().given(crawlingService).insertByCrawl(crawlingModel);
                String crawlingModelJson = objectMapper.writeValueAsString(crawlingModel);

                // when & then
                mvc.perform(post("/crawling/post/insert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crawlingModelJson))
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(crawlingService).insertByCrawl(crawlingModel);

                String crawlingModelJson = objectMapper.writeValueAsString(crawlingModel);

                // when & then
                mvc.perform(post("/crawling/post/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crawlingModelJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());


                then(crawlingService).should(times(1)).insertByCrawl(crawlingModel);
            }

            @Test
            @DisplayName("실패(WinningReport 예외)")
            void fail_winningReport() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(crawlingService).insertByCrawl(crawlingModel);

                String crawlingModelJson = objectMapper.writeValueAsString(crawlingModel);

                // when & then
                mvc.perform(post("/crawling/post/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crawlingModelJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());


                then(crawlingService).should(times(1)).insertByCrawl(crawlingModel);
            }
        }

        @Nested
        @DisplayName("InsertAllByCrawl 테스트")
        class Test_InsertAllByCrawl {
            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                List<CrawlingModel> crawlingModelList = new ArrayList<>(Arrays.asList(crawlingModel));

                willDoNothing().given(crawlingService).insertAllByCrawl(crawlingModelList);
                String crawlingModelListJson = objectMapper.writeValueAsString(crawlingModelList);

                // when & then
                mvc.perform(post("/crawling/post/insert/list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crawlingModelListJson))
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("실패(Result 예외)")
            void fail_result() throws Exception {
                // given
                List<CrawlingModel> crawlingModelList = new ArrayList<>(Arrays.asList(crawlingModel));
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT;

                willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(crawlingService).insertAllByCrawl(crawlingModelList);

                String crawlingModelListJson = objectMapper.writeValueAsString(crawlingModelList);

                // when & then
                mvc.perform(post("/crawling/post/insert/list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crawlingModelListJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());


                then(crawlingService).should(times(1)).insertAllByCrawl(crawlingModelList);
            }

            @Test
            @DisplayName("실패(WinningReport 예외)")
            void fail_winningReport() throws Exception {
                // given
                List<CrawlingModel> crawlingModelList = new ArrayList<>(Arrays.asList(crawlingModel));
                ErrorCode errorCode = ErrorCode.NOT_EXIST_WINNING_REPORT;

                willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(crawlingService).insertAllByCrawl(crawlingModelList);

                String crawlingModelListJson = objectMapper.writeValueAsString(crawlingModelList);

                // when & then
                mvc.perform(post("/crawling/post/insert/list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(crawlingModelListJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());


                then(crawlingService).should(times(1)).insertAllByCrawl(crawlingModelList);
            }
        }
    }
}
