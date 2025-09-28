package com.health.app.domain.health;

import java.util.Objects;


public class Steps {

    private final int value;

    public Steps(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Steps cannot be negative");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Steps add(Steps other) {
        return new Steps(this.value + other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Steps steps = (Steps) obj;
        return value == steps.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
