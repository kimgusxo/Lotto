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

        @Nested
        @DisplayName("calcStatLotto 테스트")
        class Test_CalcStatLotto {

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

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                List<StatLottoDTO> statLottoDTOList = new ArrayList<>(Arrays.asList(statLottoDTO));
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

    }

}
