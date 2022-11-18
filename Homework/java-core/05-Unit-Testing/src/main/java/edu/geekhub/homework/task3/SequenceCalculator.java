package edu.geekhub.homework.task3;


import static java.util.Objects.requireNonNull;

/**
 * Arithmetic operation calculator for a sequence of values<br/>
 * As real world example try to input: 1*2*3 in a Chrome address bar
 */
public class SequenceCalculator {

    /**
     * Takes an input extract valid integers and calculate them using selected operation.<br/>
     * <p>
     * Example of work:
     * <pre>
     *     input: 1, 2, 3, 4
     *     operation: '*'
     *     result: 1 * 2 * 3 * 4 = 24
     * </pre>
     *
     * @param input     that contains a comma - ',' separated characters
     * @param operation {@link ArithmeticOperation} that should be applied to input numbers
     * @return result of calculation
     */
    int calculate(String input, ArithmeticOperation operation) {
        validateInputState(input);
        validateOperationState(operation);
        int[] numbers = getNumbersFromInput(input);
        return operation.apply(numbers);
    }

    private void validateInputState(String inputState) {
        checkInputStateIsPresent(inputState);
        checkInputStateContainsNumbers(inputState);
    }

    private static void checkInputStateIsPresent(String inputState) {
        requireNonNull(inputState);

        if (inputState.isBlank()) {
            throw new IllegalArgumentException(
                "Cant process empty input state"
            );
        }
    }

    private static void checkInputStateContainsNumbers(String inputState) {
        boolean isContainsNumbers = false;
        for (int i = 0; i < inputState.length(); i++) {
            if (Character.isDigit(inputState.charAt(i))) {
                isContainsNumbers = true;
                break;
            }
        }
        if (!isContainsNumbers) {
            throw new IllegalArgumentException(
                "Cant process input state without numbers"
            );
        }
    }

    private static void validateOperationState(ArithmeticOperation operation) {
        requireNonNull(operation);
    }

    private static int[] getNumbersFromInput(String input) {
        String[] separateInput = input.split(",");
        String[] stringNumbers = new String[separateInput.length];
        for (int i = 0; i < separateInput.length; i++) {
            stringNumbers[i] = "";
            for (int j = 0; j < separateInput[i].length(); j++) {
                if (Character.isDigit(separateInput[i].charAt(j))) {
                    stringNumbers[i] += separateInput[i].charAt(j);
                }
            }
        }

        int sizeOfIntArray = 0;
        for (var it : stringNumbers) {
            if (!it.isBlank()) {
                sizeOfIntArray++;
            }
        }

        int[] array = new int[sizeOfIntArray];
        int i = 0;
        for (String it : stringNumbers) {
            if (!it.isBlank()) {
                array[i] = Integer.parseInt(it);
                i++;
            }
        }

        return array;
    }
}
