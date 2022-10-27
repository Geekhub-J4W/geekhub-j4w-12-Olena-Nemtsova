package edu.geekhub.homework;


import java.util.Scanner;

public class ApplicationStarter {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String text;
        while(true) {
            System.out.println("Enter int number:");
            text = s.next();
            try {
                int n = Integer.parseInt(text);
                System.out.printf("Result: %.2f %n", calculate(n));
                s.close();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error! "+e.getMessage());
            }
        }
    }

    private static double calculate(int n) {
        if (n % 2 == 0) return Math.pow(n, 2);
        else if (n % 3 == 0) return Math.PI * Math.pow(n, 2);
        else return Math.pow(n, 2) * Math.sqrt(3) / 4;
    }
}
