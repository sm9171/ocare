package com.health.app.domain.health;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Steps 값 객체 테스트")
class StepsTest {

    @Test
    @DisplayName("유효한 걸음 수로 Steps 생성 성공")
    void shouldCreateValidSteps() {
        // given & when
        Steps steps = new Steps(1000);

        // then
        assertThat(steps.getValue()).isEqualTo(1000);
    }

    @Test
    @DisplayName("0으로 Steps 생성 성공")
    void shouldCreateStepsWithZero() {
        // given & when
        Steps steps = new Steps(0);

        // then
        assertThat(steps.getValue()).isZero();
    }

    @Test
    @DisplayName("음수 걸음 수로 생성 시 예외 발생")
    void shouldThrowExceptionForNegativeSteps() {
        // when & then
        assertThatThrownBy(() -> new Steps(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Steps cannot be negative");
    }

    @Test
    @DisplayName("매우 큰 걸음 수로 생성 성공")
    void shouldCreateStepsWithLargeValue() {
        // given
        int largeValue = Integer.MAX_VALUE;

        // when & then
        assertThatNoException().isThrownBy(() -> {
            Steps steps = new Steps(largeValue);
            assertThat(steps.getValue()).isEqualTo(largeValue);
        });
    }

    @Test
    @DisplayName("두 Steps 객체 더하기 성공")
    void shouldAddStepsCorrectly() {
        // given
        Steps steps1 = new Steps(1000);
        Steps steps2 = new Steps(2000);

        // when
        Steps result = steps1.add(steps2);

        // then
        assertThat(result.getValue()).isEqualTo(3000);
        assertThat(steps1.getValue()).isEqualTo(1000); // 불변성 확인
        assertThat(steps2.getValue()).isEqualTo(2000); // 불변성 확인
    }

    @Test
    @DisplayName("0과 더하기")
    void shouldAddZeroSteps() {
        // given
        Steps steps1 = new Steps(1000);
        Steps steps2 = new Steps(0);

        // when
        Steps result = steps1.add(steps2);

        // then
        assertThat(result.getValue()).isEqualTo(1000);
    }

    @Test
    @DisplayName("같은 값의 Steps 객체들은 equals에서 true")
    void shouldImplementEqualsCorrectly() {
        // given
        Steps steps1 = new Steps(1000);
        Steps steps2 = new Steps(1000);
        Steps steps3 = new Steps(2000);

        // when & then
        assertThat(steps1)
                .isEqualTo(steps2)
                .isNotEqualTo(steps3);
        assertThat(steps2).isNotEqualTo(steps3);
    }

    @Test
    @DisplayName("자기 자신과 equals 비교")
    void shouldEqualsSelf() {
        // given
        Steps steps = new Steps(1000);

        // when & then
        assertThat(steps).isEqualTo(steps);
    }

    @Test
    @DisplayName("null과 equals 비교")
    void shouldNotEqualsNull() {
        // given
        Steps steps = new Steps(1000);

        // when & then
        assertThat(steps).isNotEqualTo(null);
    }

    @Test
    @DisplayName("다른 타입 객체와 equals 비교")
    void shouldNotEqualsDifferentType() {
        // given
        Steps steps = new Steps(1000);

        // when & then
        assertThat(steps)
                .isNotEqualTo("1000")
                .isNotEqualTo(1000);
    }

    @Test
    @DisplayName("같은 값의 Steps 객체들은 같은 hashCode")
    void shouldImplementHashCodeCorrectly() {
        // given
        Steps steps1 = new Steps(1000);
        Steps steps2 = new Steps(1000);

        // when & then
        assertThat(steps1).hasSameHashCodeAs(steps2);
    }

    @Test
    @DisplayName("다른 값의 Steps 객체들은 다른 hashCode (일반적으로)")
    void shouldHaveDifferentHashCodeForDifferentValues() {
        // given
        Steps steps1 = new Steps(1000);
        Steps steps2 = new Steps(2000);

        // when & then
        assertThat(steps1.hashCode()).isNotEqualTo(steps2.hashCode());
    }

    @Test
    @DisplayName("toString은 값을 문자열로 반환")
    void shouldImplementToStringCorrectly() {
        // given
        Steps steps = new Steps(1000);

        // when
        String result = steps.toString();

        // then
        assertThat(result).isEqualTo("1000");
    }

    @Test
    @DisplayName("toString은 0을 올바르게 반환")
    void shouldToStringForZero() {
        // given
        Steps steps = new Steps(0);

        // when
        String result = steps.toString();

        // then
        assertThat(result).isEqualTo("0");
    }
}