package edu.geekhub.homework;

import java.util.Scanner;

public class ApplicationStarter {

    public static void main(String[] args) {
        Figure[] figures = new Figure[2];

        for (int i = 0; i < 2; i++) {
            int choice;

            choice = getChoiceInput("Choose figure #" + (i + 1) + ":\n1.Circle\n2.Triangle\n3.Square\n4.Rectangle");
            switch (choice) {
                case 1 -> figures[i] = new Circle(getDoubleInput("Enter radius:"));
                case 2 -> figures[i] = new Triangle(getDoubleInput("Enter side:"));
                case 3 -> figures[i] = new Square(getDoubleInput("Enter side:"));
                default -> figures[i] = new Rectangle(getDoubleInput("Enter width:"), getDoubleInput("Enter height:"));
            }

            choice = getChoiceInput("Choose a color:\n1.Red\n2.Blue\n3.Orange\n4.Violet\n5.Default");
            switch (choice) {
                case 1 -> figures[i].setColor("Red");
                case 2 -> figures[i].setColor("Blue");
                case 3 -> figures[i].setColor("Orange");
                case 4 -> figures[i].setColor("Violet");
                default -> figures[i].setColor();
            }
        }
        print(figures[0] + "\n" + figures[1]);

        if (figures[0].getS() > figures[1].getS()) {
            print("Figure 1 is bigger than figure 2.");
        } else if (figures[0].getS() == figures[1].getS() && figures[0].getP() > figures[1].getP()) {
            print("Figure 1 is bigger than figure 2.");
        } else if (figures[0].getS() == figures[1].getS() && figures[0].getP() == figures[1].getP()) {
            print("Figure 1 is equal to figure 2.");
        } else {
            print("Figure 2 is bigger than figure 1.");
        }
    }

    static int getChoiceInput(String message) {
        int choicesCount = (int) message.lines().count() - 1;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            print(message);
            try {
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Error! Incorrectly entered choice!");
                }

                int choice = scanner.nextInt();
                if (choice > choicesCount || choice < 0) {
                    throw new IllegalArgumentException("Error! Incorrectly entered choice!");
                }
                return choice;
            } catch (IllegalArgumentException ex) {
                print(ex.getMessage());
            }
        }
    }

    static double getDoubleInput(String message) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            print(message);
            try {
                if (!scanner.hasNextDouble()) {
                    throw new IllegalArgumentException("Error! Incorrectly entered data!");
                }

                double doubleInput = scanner.nextDouble();
                if (doubleInput <= 0) {
                    throw new IllegalArgumentException("Error! Incorrectly entered data!");
                }
                return doubleInput;
            } catch (IllegalArgumentException ex) {
                print(ex.getMessage());
            }
        }
    }

    static void print(Object objectToPrint) {
        System.out.println(objectToPrint);
    }
}
