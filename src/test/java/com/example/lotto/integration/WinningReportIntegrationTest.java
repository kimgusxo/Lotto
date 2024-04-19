package com.example.lotto.integration;

import com.example.lotto.domain.Rank;
import com.example.lotto.domain.dto.RankDTO;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.repository.WinningReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WinningReportIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WinningReportRepository winningReportRepository;

    private WinningReportDTO winningReportDTO;

    private RankDTO rankDTO;

    @BeforeEach
    @DisplayName("데이터 생성")
    void setUp() {
        Integer round = 1111;
        LocalDate date = LocalDate.parse("2024-03-16");
        Long totalWinningAmount = 116382835000L;
        RankDTO rankDTO = RankDTO.builder()
                .ranking(1)
                .winningCount(16)
                .totalWinningAmount(27434600640L)
                .winningAmount(1714662540L)
                .build();

        List<RankDTO> rankDTOList = Arrays.asList(rankDTO);
    }

    @Nested
    @DisplayName("GET API 테스트")
    class Test_GET {

        @Nested
        @DisplayName("getByRound 테스트")
        class Test_GetByRound {

            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패(Validation 예외")
            void fail_valid() {
                // given

                // when

                // then

            }

        }

        @Nested
        @DisplayName("getByDate 테스트")
        class Test_GetByDate {
            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패(Validation 예외")
            void fail_valid() {
                // given

                // when

                // then

            }
        }

        @Nested
        @DisplayName("getByTotalWinningAmount 테스트")
        class Test_GetByTotalWinningAmount {
            @Test
            @DisplayName("성공")
            void success() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패")
            void fail() {
                // given

                // when

                // then

            }

            @Test
            @DisplayName("실패(Validation 예외")
            void fail_valid() {
                // given

                // when

                // then

            }
        }

    }

    @Nested
    @DisplayName("POST API 테스트")
    class Test_POST {

        @Nested
        @DisplayName("insert 테스트")
        class Test_Insert {

        }

    }

    @Nested
    @DisplayName("PUT API 테스트")
    class Test_PUT {

        @Nested
        @DisplayName("update 테스트")
        class Test_Update {

        }

    }

    @Nested
    @DisplayName("DELETE API 테스트")
    class Test_DELETE {

        @Nested
        @DisplayName("delete 테스트")
        class Test_Delete {

        }

    }

    @AfterEach
    @DisplayName("데이터 삭제")
    void clean() {
        winningReportRepository.deleteAll();
    }

}
