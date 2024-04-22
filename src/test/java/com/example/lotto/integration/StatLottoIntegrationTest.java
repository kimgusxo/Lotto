package com.example.lotto.integration;

import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.error.ErrorDTO;
import com.example.lotto.repository.StatLottoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatLottoIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StatLottoRepository statLottoRepository;

    private StatLottoDTO statLottoDTO;

    @BeforeEach
    @DisplayName("데이터 설정")
    void setUp() {
        Integer number = 1;
        Integer count = 154;
        Double probability = 2.3081534772182253;
        Integer bonusCount = 31;
        Double bonusProbability = 2.787769784172662;

        statLottoDTO = StatLottoDTO.builder()
                .number(number)
                .count(count)
                .probability(probability)
                .bonusCount(bonusCount)
                .bonusProbability(bonusProbability)
                .build();
    }

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        @Nested
        @DisplayName("calcStatLotto 테스트")
        class Test_CalcStatLotto {

            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when
                ResponseEntity<StatLottoDTO[]> response =
                        testRestTemplate.getForEntity("/statLotto/get/calc", StatLottoDTO[].class);

                List<StatLottoDTO> statLottoDTOList = Arrays.asList(response.getBody());

                // then
                statLottoListAssertThat(response, statLottoDTO, statLottoDTOList, HttpStatus.OK);

                // log
                logger(statLottoDTOList.get(0));

            }
        }

        @Nested
        @DisplayName("getByNumber 테스트")
        class Test_GetByNumber {

            @BeforeEach
            @DisplayName("데이터 생성")
            void create() {
                statLottoRepository.save(statLottoDTO.toEntity());
            }

            @Test
            @DisplayName("성공")
            void success() {
                // given
                Integer number = 1;

                // when
                ResponseEntity<StatLottoDTO> response =
                        testRestTemplate.getForEntity("/statLotto/get/number/" + number, StatLottoDTO.class);

                StatLottoDTO findStatLottoDTO = response.getBody();

                // then
                statLottoAssertThat(response, statLottoDTO, findStatLottoDTO, HttpStatus.OK);

                // log
                logger(statLottoDTO);
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                Integer notExistNumber = 46;

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/statLotto/get/number/" + notExistNumber, ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

        }

        @Nested
        @DisplayName("getByNumberList 테스트")
        class Test_GetByNumberList {

            @BeforeEach
            @DisplayName("데이터 생성")
            void create() {
                statLottoRepository.save(statLottoDTO.toEntity());
            }

            @Test
            @DisplayName("성공")
            void success() {
                // given
                List<Integer> numberList = Arrays.asList(1);

                // when
                ResponseEntity<StatLottoDTO[]> response =
                        testRestTemplate.getForEntity("/get/numberList?numberList=" + numberList.get(0), StatLottoDTO[].class);

                List<StatLottoDTO> statLottoDTOList = Arrays.asList(response.getBody());

                // then
                statLottoListAssertThat(response, statLottoDTO, statLottoDTOList, HttpStatus.OK);

                // log
                logger(statLottoDTOList.get(0));
            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                List<Integer> numberList = Arrays.asList(46, 47, 48);

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/get/numberList?numberList=" + numberList.get(0) +
                                "&numberList=" + numberList.get(1) + "&numberList= " + numberList.get(2), ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

        }

        @Nested
        @DisplayName("getAll 테스트")
        class Test_GetAll {

            @BeforeEach
            @DisplayName("데이터 생성")
            void create() {
                statLottoRepository.save(statLottoDTO.toEntity());
            }

            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when
                ResponseEntity<StatLottoDTO[]> response =
                        testRestTemplate.getForEntity("/statLotto/get/all", StatLottoDTO[].class);

                List<StatLottoDTO> statLottoDTOList = Arrays.asList(response.getBody());

                // then
                statLottoListAssertThat(response, statLottoDTO, statLottoDTOList, HttpStatus.OK);

                // log
                logger(statLottoDTOList.get(0));

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given
                statLottoRepository.deleteAll();

                // when
                ResponseEntity<ErrorDTO> errorResponse =
                        testRestTemplate.getForEntity("/statLotto/get/all", ErrorDTO.class);

                ErrorDTO errorDTO = errorResponse.getBody();
                ErrorCode errorCode = ErrorCode.NOT_EXIST_STAT_LOTTO_LIST;

                // then
                errorAssertThat(errorResponse, errorDTO, errorCode);

                // log
                errorLogger(errorDTO);

            }

        }

    }

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        statLottoRepository.deleteAll();
    }

    // statLotto assertThat 함수
    void statLottoAssertThat(ResponseEntity<StatLottoDTO> response, StatLottoDTO inputStatLottoDTO,
                          StatLottoDTO outputStatLottoDTO, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputStatLottoDTO.getNumber())
                .isEqualTo(inputStatLottoDTO.getNumber());
        assertThat(outputStatLottoDTO.getCount())
                .isEqualTo(inputStatLottoDTO.getCount());
        assertThat(outputStatLottoDTO.getProbability())
                .isEqualTo(inputStatLottoDTO.getProbability());
        assertThat(outputStatLottoDTO.getBonusCount())
                .isEqualTo(inputStatLottoDTO.getBonusCount());
        assertThat(outputStatLottoDTO.getBonusProbability())
                .isEqualTo(inputStatLottoDTO.getBonusProbability());
    }

    // statLottoList assertThat 함수
    void statLottoListAssertThat(ResponseEntity<StatLottoDTO[]> response, StatLottoDTO inputStatLottoDTO,
                              List<StatLottoDTO> outputStatLottoDTOList, HttpStatus httpStatus) {
        assertThat(response)
                .isNotNull();

        assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);

        assertThat(outputStatLottoDTOList)
                .isNotEmpty();

        assertThat(outputStatLottoDTOList.get(0).getNumber())
                .isEqualTo(inputStatLottoDTO.getNumber());
        assertThat(outputStatLottoDTOList.get(0).getCount())
                .isEqualTo(inputStatLottoDTO.getCount());
        assertThat(outputStatLottoDTOList.get(0).getProbability())
                .isEqualTo(inputStatLottoDTO.getProbability());
        assertThat(outputStatLottoDTOList.get(0).getBonusCount())
                .isEqualTo(inputStatLottoDTO.getBonusCount());
        assertThat(outputStatLottoDTOList.get(0).getBonusProbability())
                .isEqualTo(inputStatLottoDTO.getBonusProbability());
    }

    // error assertThat 함수
    void errorAssertThat(ResponseEntity<ErrorDTO> errorResponse, ErrorDTO errorDTO, ErrorCode errorCode) {
        assertThat(errorResponse)
                .isNotNull();

        assertThat(errorResponse.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(errorDTO.getCode())
                .isEqualTo(errorCode.getCode());
        assertThat(errorDTO.getDetail())
                .isEqualTo(errorCode.getDetail());
    }

    // 정상 일때 logger
    void logger(StatLottoDTO statLottoDTO) {
        log.info("number: {}", statLottoDTO.getNumber());
        log.info("count: {}", statLottoDTO.getCount());
        log.info("probability: {}", statLottoDTO.getProbability());
        log.info("bonusCount: {}", statLottoDTO.getBonusCount());
        log.info("bonusProbability: {}", statLottoDTO.getBonusProbability());
    }

    // 에러 일때 logger
    void errorLogger(ErrorDTO errorDTO) {
        log.info("code: {}", errorDTO.getCode());
        log.info("detail: {}", errorDTO.getDetail());
    }


}
