package com.example.lotto.unit.simulation.service;

import com.example.lotto.simulation.machine.LottoMachine;
import com.example.lotto.simulation.model.LottoNumber;
import com.example.lotto.simulation.service.LottoMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LottoMachineServiceUnitTest {

    @Mock
    private LottoMachine lottoMachine;

    @InjectMocks
    private LottoMachineService lottoMachineService;

    @Nested
    @DisplayName("Drawn 테스트")
    class Test_Drawn {

        private LottoNumber lottoNumber;

        @BeforeEach
        @DisplayName("데이터 설정")
        void setUp() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
            Integer bonusNumber = 7;

            lottoNumber = new LottoNumber();
            lottoNumber.setNumbers(numbers);
            lottoNumber.setBonusNumber(bonusNumber);
        }

        @Test
        @DisplayName("성공")
        void success() {
            // given
            given(lottoMachine.drawLottoNumbers()).willReturn(lottoNumber);

            // when
            LottoNumber drawnLottoNumbers = lottoMachineService.drawNumbers();

            // then
            assertThat(drawnLottoNumbers.getNumbers())
                    .isEqualTo(lottoNumber.getNumbers());
            assertThat(drawnLottoNumbers.getBonusNumber())
                    .isEqualTo(lottoNumber.getBonusNumber());

            then(lottoMachine).should(times(1)).drawLottoNumbers();

        }

    }

}
