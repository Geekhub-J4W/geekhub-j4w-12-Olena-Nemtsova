package edu.geekhub.homework;

public abstract class Figure implements Colorful {
    String color, type;
    static int FigureNumber = 1;
    int id;

    Figure(String type) {
        this.type = type;
        id = FigureNumber++;
        color = GetColor();
    }

    public void setColor(String color) {
        this.color = color;
    }

    public abstract double GetP();

    public abstract double GetS();

    @Override
    public String toString() {
        return "Figure #" + id;
    }
}
