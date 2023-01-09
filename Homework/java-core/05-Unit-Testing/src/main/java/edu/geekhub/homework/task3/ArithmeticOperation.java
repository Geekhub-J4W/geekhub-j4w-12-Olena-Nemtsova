package edu.geekhub.homework.task3;

/**
 * Enum represents
 * <a href="https://en.wikipedia.org/wiki/Arithmetic#Arithmetic_operations">Arithmetic operations</a>
 * '+', '-', '*' and '/'
 */
public enum ArithmeticOperation {
    ADD {
        public int apply(int[] arrayOfNumbers) {
            int result = 0;
            for (int it : arrayOfNumbers) {
                result += it;
            }
            return result;
        }
    },
    SUBTRACT {
        public int apply(int[] arrayOfNumbers) {
            int result = arrayOfNumbers[0];
            for (int i = 1; i < arrayOfNumbers.length; i++) {
                result -= arrayOfNumbers[i];
            }
            return result;
        }
    },
    MULTIPLY {
        public int apply(int[] arrayOfNumbers) {
            int result = 1;
            for (int it : arrayOfNumbers) {
                result *= it;
            }
            return result;
        }
    },
    DIVISION {
        public int apply(int[] arrayOfNumbers) {
            int result = arrayOfNumbers[0];
            for (int i = 1; i < arrayOfNumbers.length; i++) {
                if (arrayOfNumbers[i] == 0) {
                    throw new ArithmeticException("division by zero");
                }
                result /= arrayOfNumbers[i];
            }
            return result;
        }
    };

    public abstract int apply(int[] arrayOfNumbers);
}
