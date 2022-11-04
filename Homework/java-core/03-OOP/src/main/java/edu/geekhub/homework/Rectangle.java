package edu.geekhub.homework;

public class Rectangle extends Figure {
    double width, height;

    public Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override
    public double GetP() {
        return (width + height) * 2;
    }

    @Override
    public double GetS() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("%s %n%s %nP = %.2f %nS = %.2f %nColor - %s %n", super.toString(), super.type, GetP(), GetS(), super.color);
    }
}
