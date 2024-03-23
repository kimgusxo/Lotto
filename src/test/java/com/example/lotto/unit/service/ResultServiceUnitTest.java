package com.example.lotto.unit.service;

import com.example.lotto.repository.ResultRepository;
import com.example.lotto.service.ResultService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResultServiceUnitTest {

    @Mock
    private ResultRepository resultRepository;

    @InjectMocks
    private ResultService resultService;

    @Nested
    @DisplayName("Read 테스트")
    class Test_Read {

        @Nested
        @DisplayName("readByRound 테스트")
        class Test_ReadByRound {

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
        }

        @Nested
        @DisplayName("readByBonusNumber")
        class Test_ReadByBonusNumber {

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
        }

        @Nested
        @DisplayName("readByNumbers 테스트")
        class Test_ReadByNumbers {

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
        }

        @Nested
        @DisplayName("readByDate 테스트")
        class Test_ReadByDate {
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
        }

    }

    @Nested
    @DisplayName("insert 테스트")
    class Test_Insert {
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
    }

    @Nested
    @DisplayName("update 테스트")
    class Test_Update {
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
    }

    @Nested
    @DisplayName("delete 테스트")
    class Test_Delete {
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
    }

}
