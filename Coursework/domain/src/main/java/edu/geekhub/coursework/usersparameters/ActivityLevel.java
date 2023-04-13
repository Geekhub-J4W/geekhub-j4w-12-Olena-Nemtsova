package edu.geekhub.coursework.usersparameters;

public enum ActivityLevel {
    ZERO(1.2),
    LOW(1.3),
    MEDIUM(1.4),
    HIGH(1.5);

    private double coefficient;

    private ActivityLevel(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return this.coefficient;
    }

}
