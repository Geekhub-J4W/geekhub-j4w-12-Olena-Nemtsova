package edu.geekhub.homework.task3;

public class ApplicationStarter {

    public static final String NUMBERS_INPUT = "1,2,3";

    public static void main(String[] args) {
        var s = new SequenceCalculator();
        var result = s.calculate(NUMBERS_INPUT, ArithmeticOperation.ADD);

        System.out.println("Result = "+result);
    }
}
