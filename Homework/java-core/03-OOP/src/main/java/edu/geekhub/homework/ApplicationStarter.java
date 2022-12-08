package edu.geekhub.homework;

import java.util.Scanner;

public class ApplicationStarter {

    public static void main(String[] args) {
        Figure[] figures = new Figure[2];
        for (int i = 0; i < 2; i++) {
            boolean isFigureSelected = false;
            int choice;
            while (!isFigureSelected) {
                choice = GetIntData("Choose figure #" + (i + 1) + ":\n1.Circle\n2.Triangle\n3.Square\n4.Rectangle");
                switch (choice) {
                    case 1:
                        figures[i] = new Circle(GetDoubleData("Enter radius:"));
                        isFigureSelected = true;
                        break;
                    case 2:
                        figures[i] = new Triangle(GetDoubleData("Enter side:"));
                        isFigureSelected = true;
                        break;
                    case 3:
                        figures[i] = new Square(GetDoubleData("Enter side:"));
                        isFigureSelected = true;
                        break;
                    case 4:
                        figures[i] = new Rectangle(GetDoubleData("Enter width:"), GetDoubleData("Enter height:"));
                        isFigureSelected = true;
                        break;
                    default:
                        PrintError();
                        break;
                }
            }
            choice = GetIntData("Choose a color:\n1.Red\n2.Blue\n3.Orange\n4.Violet");
            switch (choice) {
                case 1:
                    figures[i].setColor("Red");
                    break;
                case 2:
                    figures[i].setColor("Blue");
                    break;
                case 3:
                    figures[i].setColor("Orange");
                    break;
                case 4:
                    figures[i].setColor("Violet");
                    break;
                default:
                    break;
            }
        }
        System.out.println(figures[0].toString() + "\n" + figures[1].toString());
        if (figures[0].GetS() > figures[1].GetS()) System.out.println("Figure 1 is bigger than figure 2.");
        else System.out.println("Figure 2 is bigger than figure 1.");
    }

    static int GetIntData(String text) {
        while (true) {
            Scanner s = new Scanner(System.in);
            System.out.println(text);
            if (s.hasNextInt()) return s.nextInt();
            else PrintError();
        }
    }

    static double GetDoubleData(String text) {
        while (true) {
            Scanner s = new Scanner(System.in);
            System.out.println(text);
            if (s.hasNextDouble()) {
                double number = s.nextDouble();
                if (number > 0) return number;
                else PrintError();
            } else PrintError();
        }
    }

    static void PrintError() {
        System.out.println("Error! Incorrectly entered data!");
    }

}
