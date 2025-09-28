package com.health.app.domain.health;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


public class Distance {

    private final BigDecimal value;

    public Distance(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Distance cannot be null or negative");
        }
        this.value = value.setScale(3, RoundingMode.HALF_UP);
    }

    public Distance(double value) {
        this(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public double getDoubleValue() {
        return value.doubleValue();
    }

    public Distance add(Distance other) {
        return new Distance(this.value.add(other.value));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Distance distance = (Distance) obj;
        return Objects.equals(value, distance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
