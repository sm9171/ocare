package com.health.app.domain.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RecordKeyTest {

    @Test
    void createRecordKey_ValidValue_Success() {
        // given
        String value = "USER_123";

        // when
        RecordKey recordKey = new RecordKey(value);

        // then
        assertThat(recordKey.getValue()).isEqualTo(value);
    }

    @Test
    void createRecordKey_NullValue_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new RecordKey(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    void createRecordKey_EmptyValue_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new RecordKey(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    void createRecordKey_WhitespaceValue_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new RecordKey("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    void equals_SameValue_ReturnsTrue() {
        // given
        RecordKey key1 = new RecordKey("USER_123");
        RecordKey key2 = new RecordKey("USER_123");

        // when & then
        assertThat(key1).isEqualTo(key2);
        assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
    }

    @Test
    void equals_DifferentValue_ReturnsFalse() {
        // given
        RecordKey key1 = new RecordKey("USER_123");
        RecordKey key2 = new RecordKey("USER_456");

        // when & then
        assertThat(key1).isNotEqualTo(key2);
    }

    @Test
    void toString_ReturnsValue() {
        // given
        String value = "USER_123";
        RecordKey recordKey = new RecordKey(value);

        // when & then
        assertThat(recordKey).isEqualTo(value);
    }
}
