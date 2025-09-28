package com.health.app.domain.health;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


public class Calories {

    private final BigDecimal value;

    public Calories(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Calories cannot be null or negative");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public Calories(double value) {
        this(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public double getDoubleValue() {
        return value.doubleValue();
    }

    public Calories add(Calories other) {
        return new Calories(this.value.add(other.value));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Calories calories = (Calories) obj;
        return Objects.equals(value, calories.value);
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
