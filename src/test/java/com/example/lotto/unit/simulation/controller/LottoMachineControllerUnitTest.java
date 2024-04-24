package com.example.lotto.unit.simulation.controller;

import com.example.lotto.simulation.controller.LottoMachineController;
import com.example.lotto.simulation.model.LottoNumber;
import com.example.lotto.simulation.service.LottoMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LottoMachineController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LottoMachineControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LottoMachineService lottoMachineService;

    @Nested
    @DisplayName("GET 테스트")
    class Test_GET {

        private LottoNumber lottoNumber;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);
            Integer bonusNumber = 7;

            lottoNumber = new LottoNumber();
            lottoNumber.setNumbers(numbers);
            lottoNumber.setBonusNumber(bonusNumber);

        }

        @Nested
        @DisplayName("getNumberDraw 테스트")
        class Test_GetNumberDraw {

            @Test
            @DisplayName("성공")
            void success() throws Exception {
                // given
                Integer count = 1;
                given(lottoMachineService.drawNumbers()).willReturn(lottoNumber);

                // when & then
                mvc.perform(get("/lottoMachine/get/count/" + count)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].numbers[0]").value(lottoNumber.getNumbers().get(0)))
                        .andExpect(jsonPath("$[0].bonusNumber").value(lottoNumber.getBonusNumber()))
                        .andExpect(status().isOk());

            }

        }

    }

}
