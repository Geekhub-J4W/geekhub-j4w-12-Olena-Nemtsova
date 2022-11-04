package edu.geekhub.homework;

public class Triangle extends Figure {
    double side;

    public Triangle(double side) {
        super("Triangle");
        this.side = side;
    }

    @Override
    public double GetP() {
        return side * 3;
    }

    @Override
    public double GetS() {
        return Math.pow(side, 2) * Math.sqrt(3) / 4;
    }

    @Override
    public String toString() {
        return String.format("%s %n%s %nP = %.2f %nS = %.2f %nColor - %s %n", super.toString(), super.type, GetP(), GetS(), super.color);
    }
}
