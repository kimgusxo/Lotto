package com.example.lotto.integration;

import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.simulation.model.LottoNumber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LottoMachineIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    @DisplayName("데이터 설정")
    void create() {

        testRestTemplate.postForEntity("/crawling/post/insert/all", null, Void.class);
        testRestTemplate.getForEntity("/statLotto/get/calc", StatLottoDTO[].class);
    }

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        @Nested
        @DisplayName("getNumberDrawn 테스트")
        class Test_GetNumberDrawn {

            int a, b, c, d, e = 0;
            Map<Integer, Integer> numberFrequency = new HashMap<>();

            @Test
            @DisplayName("1등 확률 계산 테스트")
            void draw() {

                List<Integer> first = Arrays.asList(11,13,20,21,32,44);
                Integer bonusNumber = 8;

                int maxAttempts = 10;

                for (int i = 0; i < maxAttempts; i++) {
                    Integer count = 1000;
                    ResponseEntity<LottoNumber[]> response =
                            testRestTemplate.getForEntity("/lottoMachine/get/count/" + count, LottoNumber[].class);

                    List<LottoNumber> lottoNumberList = Arrays.asList(response.getBody());

                    for (LottoNumber lottoNumber : lottoNumberList) {
                        int matchedNumbers = (int) first.stream()
                                .filter(lottoNumber.getNumbers()::contains)
                                .count();

                        boolean isBonusMatched = lottoNumber.getBonusNumber() == bonusNumber;
                        determinePrize(matchedNumbers, isBonusMatched);
                    }
                }

                System.out.println("1등: " + a);
                System.out.println("2등: " + b);
                System.out.println("3등: " + c);
                System.out.println("4등: " + d);
                System.out.println("5등: " + e);



            }

            private void determinePrize(int matchedNumbers, boolean isBonusMatched) {
                if (matchedNumbers == 6) {
                    a++;
                    log.info("1등 당첨!");
                } else if (matchedNumbers == 5 && isBonusMatched) {
                    log.info("2등 당첨!");
                    b++;
                } else if (matchedNumbers == 5) {
                    c++;
                    log.info("3등 당첨!");
                } else if (matchedNumbers == 4) {
                    d++;
                    log.info("4등 당첨!");
                } else if (matchedNumbers == 3) {
                    e++;
                    log.info("5등 당첨!");
                } else {
                    log.info("낙첨");
                }
            }

            @Test
            @DisplayName("가중치 적용 테스트")
            void weightEffect() {
                int maxAttempts = 100000;

                for (int i = 0; i < maxAttempts; i++) {
                    ResponseEntity<LottoNumber[]> response =
                            testRestTemplate.getForEntity("/lottoMachine/get/count/" + 1, LottoNumber[].class);

                    Arrays.stream(response.getBody())
                            .flatMap(lottoNumber -> Stream.concat(lottoNumber.getNumbers().stream(), Stream.of(lottoNumber.getBonusNumber())))
                            .forEach(number -> numberFrequency.merge(number, 1, Integer::sum));
                }

                numberFrequency.forEach((number, count) -> log.info("Number: " + number + ", Count: " + count));

            }

        }

    }

}
