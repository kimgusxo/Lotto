package com.example.lotto.unit.controller;

import com.example.lotto.controller.StatLottoController;
import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.service.StatLottoService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatLottoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StatLottoControllerUnitTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatLottoService statLottoService;

    private StatLottoDTO statLottoDTO;

    @Nested
    @DisplayName("GET 테스트")
    class Test_GET {

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            Integer number = 1;
            Integer count = 1;
            Double probability = 1.11111;
            Integer bonusCount = 1;
            Double bonusProbability = 1.11111;

            statLottoDTO = StatLottoDTO.builder()
                    .number(number)
                    .count(count)
                    .probability(probability)
                    .bonusCount(bonusCount)
                    .bonusProbability(bonusProbability)
                    .build();
        }

        @Nested
        @DisplayName("calcStatLotto 테스트")
        class Test_CalcStatLotto {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                List<StatLottoDTO> statLottoDTOList = Arrays.asList(statLottoDTO);
                given(statLottoService.calcStatLotto()).willReturn(statLottoDTOList);

                // when & then
                mvc.perform(get("/statLotto/get/calc")
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].number").value(statLottoDTOList.get(0).getNumber()))
                        .andExpect(jsonPath("$[0].count").value(statLottoDTOList.get(0).getCount()))
                        .andExpect(jsonPath("$[0].probability").value(statLottoDTOList.get(0).getProbability()))
                        .andExpect(jsonPath("$[0].bonusCount").value(statLottoDTOList.get(0).getBonusCount()))
                        .andExpect(jsonPath("$[0].bonusProbability").value(statLottoDTOList.get(0).getBonusProbability()))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                List<StatLottoDTO> statLottoDTOList = new ArrayList<>();
                ErrorCode errorCode = ErrorCode.UNKNOWN;
                given(statLottoService.calcStatLotto()).willThrow(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode));

                // when & then
                mvc.perform(get("/statLotto/get/calc")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isInternalServerError());

            }
        }

        @Nested
        @DisplayName("getByNumber 테스트")
        class Test_GetByNumber {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer number = 1;

                given(statLottoService.readByNumber(number)).willReturn(statLottoDTO);

                // when & then
                mvc.perform(get("/statLotto/get/number/" + number)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.number").value(number))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                Integer number = -1;
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO;

                given(statLottoService.readByNumber(number)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/statLotto/get/number/" + number)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());

            }

        }

        @Nested
        @DisplayName("getByNumberList 테스트")
        class Test_GetByNumberList {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                List<Integer> numberList = Arrays.asList(1, 2 ,3);
                List<StatLottoDTO> statLottoDTOList = Arrays.asList(statLottoDTO);

                given(statLottoService.readByNumberList(numberList)).willReturn(statLottoDTOList);

                // when & then
                mvc.perform(get("/statLotto/get/numberList")
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("numberList", "1", "2", "3"))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].number").value(statLottoDTO.getNumber()))
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                List<Integer> numberList = Arrays.asList(1, 2 ,3);
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO;

                given(statLottoService.readByNumberList(numberList)).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/statLotto/get/numberList")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("numberList", "1", "2", "3"))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }

        }

        @Nested
        @DisplayName("getAll 테스트")
        class Test_GetAll {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                List<StatLottoDTO> statLottoDTOList = Arrays.asList(statLottoDTO);

                given(statLottoService.readAll()).willReturn(statLottoDTOList);

                // when & then
                mvc.perform(get("/statLotto/get/all")
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].number").value(statLottoDTO.getNumber()))
                        .andExpect(status().isOk());

            }

            @Test
            @DisplayName("실패")
            void fail() throws Exception {
                // given
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO_LIST;

                given(statLottoService.readAll()).willThrow(new CustomException(HttpStatus.BAD_REQUEST, errorCode));

                // when & then
                mvc.perform(get("/statLotto/get/all")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                        .andExpect(jsonPath("$.detail").value(errorCode.getDetail()))
                        .andExpect(status().isBadRequest());
            }

        }

    }

}
