package com.health.app.domain.common;

import java.util.Objects;

public class RecordKey {

    private final String value;

    public RecordKey(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("RecordKey cannot be null or empty");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RecordKey recordKey = (RecordKey) obj;
        return Objects.equals(value, recordKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
