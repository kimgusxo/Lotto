package com.example.lotto.unit.controller;

import com.example.lotto.controller.StatLottoController;
import com.example.lotto.service.StatLottoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatLottoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StatLottoControllerUnitTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatLottoService statLottoService;

    @Nested
    @DisplayName("GET 테스트")
    class Test_GET {

        @Nested
        @DisplayName("calcStatLotto 테스트")
        class Test_CalcStatLotto {

            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when & then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given

                // when & then

            }
        }

    }

}
