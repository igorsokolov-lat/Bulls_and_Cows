package bullscows;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String possibleSymbols = "0123456789abcdefghijklmnopqrstuvwxyz";
        System.out.println("Input the length of the secret code:");
        String numberLength = scanner.nextLine();
        boolean numberLengthNumeric = numberLength.matches("-?\\d+(\\.\\d+)?");
        if (!numberLengthNumeric) {
            System.out.println("Error: \"" + numberLength + "\" isn't a valid number.");
        } else if (Integer.parseInt(numberLength) < 1) {
            System.out.println("Error");
        } else {
            System.out.println("Input the number of possible symbols in the code");
            String numberOfSymbols = scanner.nextLine();
            boolean numberOfSymbolsNumeric = numberOfSymbols.matches("-?\\d+(\\.\\d+)?");
            if (!numberOfSymbolsNumeric) {
                System.out.println("Error: \"" + numberOfSymbols + "\" isn't a valid number.");
            } else {
                if (Integer.parseInt(numberOfSymbols) > 36) {
                    System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                } else if (Integer.parseInt(numberOfSymbols) < Integer.parseInt(numberLength)) {
                    System.out.println("Error: it's not possible to generate a code with a length of " + numberLength + " with " + numberOfSymbols + " unique symbols.");
                } else {
                    int numberLengthInt = Integer.parseInt(numberLength);
                    BigInteger numberOfSymbolsBigInt = new BigInteger(numberOfSymbols);
                    StringBuilder secretCode = checkIfUnique(numberLengthInt, numberOfSymbolsBigInt);
                    System.out.print("The secret is prepared: ");
                    for (int i = 0; i < numberLengthInt; i++) {
                        System.out.print("*");
                    }
                    if (numberOfSymbolsBigInt.compareTo(BigInteger.TEN) < 0) {
                        System.out.println(" (0-" + possibleSymbols.charAt(numberOfSymbolsBigInt.intValue()) + ")");
                    } else if (numberOfSymbolsBigInt.compareTo(BigInteger.TEN) == 0) {
                        System.out.println(" (0-9, a)");
                    } else {
                        System.out.println(" (0-9, a-" + possibleSymbols.charAt(numberOfSymbolsBigInt.intValue() - 1) + ").");
                    }

                    checkSecretCode(numberLengthInt, secretCode);
                }
            }
        }
    }

    public static StringBuilder generatePseudoRandom(int numberLength) {
        double randomBound = Math.pow(10, numberLength);
        double randomNumber = Math.random() * randomBound;
        StringBuilder stringRandomNumber = new StringBuilder(String.valueOf((int) randomNumber));
        return stringRandomNumber;
    }

    public static StringBuilder checkIfUnique(int numberLength, BigInteger numberOfSymbols) {
        boolean equalSymbols = true;
        StringBuilder randomNumber = null;
        while (equalSymbols) {
            randomNumber = convertToSecretCode(numberLength, numberOfSymbols);
            for (int i = 0; i < randomNumber.length(); i++) {
                for (int j = 0; j < randomNumber.length(); j++) {
                    if (i == j) {
                        equalSymbols = false;
                        continue;
                    } else if (randomNumber.charAt(i) != randomNumber.charAt(j)) {
                        equalSymbols = false;
                    } else if (randomNumber.charAt(i) == randomNumber.charAt(j)) {
                        equalSymbols = true;
                        break;
                    }
                }
                if (equalSymbols == true) {
                    break;
                }
            }
        }
        return randomNumber;
    }

    public static void checkSecretCode(int numberLength, StringBuilder secretCode) {
        Scanner scanner = new Scanner(System.in);
        int bulls = 0;
        int cows = 0;
        String bullsWord = "bull";
        String cowsWord = "cow";
        int turnNumber = 1;
        boolean flag = true;

        while (flag) {
            System.out.println("Turn " + turnNumber);
            String guess = scanner.nextLine();
            for (int i = 0; i < secretCode.length(); i++) {
                for (int j = 0; j < secretCode.length(); j++) {
                    boolean compare = secretCode.charAt(i) == guess.charAt(j);
                    if (compare && i == j) {
                        bulls++;
                    } else if (compare) {
                        cows++;
                    }
                }
            }

            if (cows != 1) {
                cowsWord = "cows";
            }
            if (bulls != 1) {
                bullsWord = "bulls";
            }
            if (bulls == numberLength) {
                flag = false;
            }

            if (cows == 0) {
                System.out.println("Grade: " + bulls + " " + bullsWord);
            } else if (bulls == 0) {
                System.out.println("Grade: " + cows + " " + cowsWord);
            } else {
                System.out.println("Grade: " + bulls + " " + bullsWord + " and " + cows + " " + cowsWord);
            }
            cows = 0;
            bulls = 0;
            turnNumber++;
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }

    public static StringBuilder convertToSecretCode(int numberLength, BigInteger numberOfSymbols) {
        String baseNumber = "";
        StringBuilder secretCode = new StringBuilder();
        BigInteger decimalNumber = null;
        char charList[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        while (secretCode.length() < numberLength) {
            decimalNumber = new BigInteger(generatePseudoRandom(numberLength).toString());
            while (decimalNumber.compareTo(BigInteger.ZERO) == 1) {
                if (secretCode.length() == numberLength) {
                    break;
                } else {
                    BigInteger remainder = decimalNumber.remainder(numberOfSymbols);
                    baseNumber = charList[remainder.intValue()] + baseNumber;
                    decimalNumber = decimalNumber.divide(numberOfSymbols);
                    secretCode = new StringBuilder(baseNumber);
                }
            }
        }
        return secretCode;
    }
}