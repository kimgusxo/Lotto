package com.example.lotto.unit.controller;

import com.example.lotto.controller.ResultController;
import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.service.ResultService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResultController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ResultControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResultService resultService;

    @Nested
    @DisplayName("GET 테스트")
    class Test_GET {

        private ResultDTO resultDTO;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            resultDTO = ResultDTO.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
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
                given(resultService.readByRound(round)).willReturn(resultDTO);

                // when & then
                mvc.perform(get("/result/get/round/" + round)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.round").value(round))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer round = -1;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_ROUND_TOKEN;
                given(resultService.readByRound(round)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/result/get/round/" + round)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }

        }

        @Nested
        @DisplayName("getByBonusNumber 테스트")
        class Test_GetByBonusNumber {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer bonusNumber = 4;
                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(resultDTO));

                given(resultService.readByBonusNumber(bonusNumber)).willReturn(resultDTOList);

                // when & then
                mvc.perform(get("/result/get/bonusNumber/" + bonusNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].bonusNumber").value(bonusNumber))
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer bonusNumber = -1;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_BONUS_NUMBER_TOKEN;

                given(resultService.readByBonusNumber(bonusNumber)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/result/get/bonusNumber/" + bonusNumber)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }

        }

        @Nested
        @DisplayName("getByNumber 테스트")
        class Test_GetByNumber {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer number = 3;
                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(resultDTO));

                given(resultService.readByNumber(number)).willReturn(resultDTOList);

                // when & then
                mvc.perform(get("/result/get/number/" + number)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].numbers", hasSize(6)))
                        .andExpect(jsonPath("$[0].numbers", hasItem(number)))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer number = -1;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_NUMBER_TOKEN;

                given(resultService.readByNumber(number)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/result/get/number/" + number)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
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

                List<ResultDTO> resultDTOList = new ArrayList<>(Arrays.asList(resultDTO));

                given(resultService.readByDate(startDate, endDate)).willReturn(resultDTOList);

                // when & then
                mvc.perform(get("/result/get/date?startDate=" + startDate + "&endDate=" + endDate)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].date").value("2024-03-16"))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                LocalDate startDate = LocalDate.parse("1000-03-01");
                LocalDate endDate = LocalDate.parse("1000-03-31");
                ErrorCode errorCode = ErrorCode.INCORRECT_DATE_TOKEN;

                given(resultService.readByDate(startDate, endDate)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/result/get/date?startDate=" + startDate + "&endDate=" + endDate)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }

        }

    }

    @Nested
    @DisplayName("POST 테스트")
    class Test_POST {

        private ResultDTO resultDTO;
        private ObjectMapper objectMapper;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            resultDTO = ResultDTO.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();
        }

        @Nested
        @DisplayName("Insert 테스트")
        class Test_Insert {
            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                given(resultService.insert(resultDTO)).willReturn(resultDTO);

                String resultDTOJson = objectMapper.writeValueAsString(resultDTO);


                // when & then
                mvc.perform(post("/result/post/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(resultDTOJson))
                        .andExpect(jsonPath("$.round").value(resultDTO.getRound()))
                        .andExpect(jsonPath("$.numbers", hasSize(resultDTO.getNumbers().size())))
                        .andExpect(jsonPath("$.bonusNumber").value(resultDTO.getBonusNumber()))
                        .andExpect(jsonPath("$.date").value(resultDTO.getDate().toString()))
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_TOKEN;
                given(resultService.insert(resultDTO)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                String resultDTOJson = objectMapper.writeValueAsString(resultDTO);

                // when & then
                mvc.perform(post("/result/post/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(resultDTOJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }
        }

    }

    @Nested
    @DisplayName("PUT 테스트")
    class Test_PUT {

        private ResultDTO updateResultDTO;
        private ObjectMapper objectMapper;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            Integer round = 1111;
            List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 13, 30, 33, 43, 45));
            Integer bonusNumber = 4;
            LocalDate date = LocalDate.parse("2024-03-16");

            updateResultDTO = ResultDTO.builder()
                    .round(round)
                    .numbers(numbers)
                    .bonusNumber(bonusNumber)
                    .date(date)
                    .build();
        }

        @Nested
        @DisplayName("Update 테스트")
        class Test_Update {
            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                given(resultService.update(updateResultDTO.getRound(), updateResultDTO)).willReturn(updateResultDTO);

                String updateResultDTOJson = objectMapper.writeValueAsString(updateResultDTO);

                // when & then
                mvc.perform(put("/result/put/update/" + updateResultDTO.getRound())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateResultDTOJson))
                        .andExpect(jsonPath("$.round").value(updateResultDTO.getRound()))
                        .andExpect(jsonPath("$.numbers", hasSize(updateResultDTO.getNumbers().size())))
                        .andExpect(jsonPath("$.bonusNumber").value(updateResultDTO.getBonusNumber()))
                        .andExpect(jsonPath("$.date").value(updateResultDTO.getDate().toString()))
                        .andExpect(status().isAccepted());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_TOKEN;
                given(resultService.update(updateResultDTO.getRound(), updateResultDTO)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                String updateResultDTOJson = objectMapper.writeValueAsString(updateResultDTO);

                // when & then
                mvc.perform(put("/result/put/update/" + updateResultDTO.getRound())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateResultDTOJson))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE 테스트")
    class Test_DELETE {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // givenu
            Integer round = 1111;
            willDoNothing().given(resultService).delete(round);

            // when & then
            mvc.perform(delete("/result/delete/" + round)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            then(resultService).should(times(1)).delete(round);
        }

        @Test
        @DisplayName("실패")
        void fail() throws Exception {
            // given
            Integer round = -1;
            ErrorCode errorCode = ErrorCode.NOT_EXIST_RESULT_TOKEN;

            willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode)).given(resultService).delete(round);

            // when & then
            mvc.perform(delete("/result/delete/" + round)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());


            then(resultService).should(times(1)).delete(round);
        }
    }

}
