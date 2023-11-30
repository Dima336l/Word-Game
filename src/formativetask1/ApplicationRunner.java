package formativetask1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationRunner {
    static final int WORD_LENGTH = 3;

    public static ArrayList<String> readFile() {
        String dataFile = System.getProperty("user.dir") + File.separator + "datafile.txt";

        // Initialize list
        ArrayList<String> listOfStrings = new ArrayList<>();

        // Creating new bufffer
        try (BufferedReader bf = new BufferedReader(new FileReader(dataFile))) {
            String line = bf.readLine();

//          Reading the file line by line
            while (line != null) {
                listOfStrings.add(line);
                line = bf.readLine();
            }
//         Exception handling
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error encountered");
            System.exit(0);
        }

        return listOfStrings;
    }

    public static boolean existsInList(String input, ArrayList<String> arr) {
        boolean exists = false;
        for (String str : arr) {
            if (input.equals(str)) {
                exists = true;
//              We break since we the string was found and no point going forward
                break;
            } else {
                exists = false;
            }
        }
        return exists;
    }

    public static String handleScanner() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return input;
    }

    public static boolean isValid(String word) {
        boolean isValid = true;
        if (word.length() != WORD_LENGTH) {
            System.out.print("The word must be exactly 3 letters, please try again: ");
            isValid = false;
        } else {
            for (int i = 0; i < word.length(); i++) {
                int ascii = (int) word.charAt(i);
                    if (ascii < 97 || ascii > 122) { // Check if input is lowercase
                        isValid = false;
                        System.out.print("The word must be lowercase, please try again: ");
                        break;
                    }
            }
        }
        return isValid;
    }

//  Convert chars to their corresponding encoding
    public static int[] arrayOfDigits(String word) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < WORD_LENGTH; i++) {
            int ascii = (int) word.charAt(i) - 97 + 1;
            numbers.add(ascii);
        }
        int[] result = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i);
        }
        return result;
    }
    
    static void firstPrompt() {
        System.out.println("Let's play ...");
        System.out.println("Player 1 to choose the first word ...");
        System.out.print("Enter a 3-letter word (lower case), which must have a value of 20 or less, or less, or enter * to give up ");
    }

    static void displayPrompt(int playerNum, char chr) {
        System.out.println("Player " + playerNum + " to chose the next word ...");
        System.out.print("Guess a 3-lettered word (lower case) starting with letter " + chr + " or enter * to give up ");
    }

    static void printLines() {
        final int NUM_OF_LINES = 70;
        for (int i = 0; i < NUM_OF_LINES; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    static void displayHeader() {
        printLines();
        System.out.printf("| %-20s | %20s | %20s |%n", "word", "word total", "running total");
        printLines();
    }

    static void handleExit(String input) {
        if (input.equals("*")) {
            System.out.println("Bye Bye");
            System.exit(0);
        }
    }

//  Check to see if input exists in list and is valid, returns bool
    public static boolean validateInput(String input, ArrayList<String> arr) {
        boolean isValid = false;
        boolean existInList = existsInList(input, arr);
        boolean itsValid = isValid(input);
        handleExit(input);
        if (itsValid) {
            if (existInList) {
                isValid = true;
            } else {
                System.out.println("The word is not in the list");
            }
        }
        return isValid;
    }

    public static int sumOfDigits(String input) {
        int sum;
        int[] charValues = arrayOfDigits(input);
        sum = charValues[0] + charValues[1] + charValues[2];
        return sum;
    }

//  Get and input and check if sum is lower than 20
    public static String getFirstInput(ArrayList<String> arr) {
        boolean keepGoing;
        String input;
        int sum = 0;
        do {
            input = handleScanner();
            keepGoing = validateInput(input, arr);
            if (keepGoing) {
                sum = sumOfDigits(input);
            }
            if (sum > 20) {
                System.out.println("The sum is greater than 20, please try again.");
            }
        } while (!(keepGoing && sum < 20));
        return input;
    }

    public static int calculateRunningTotal(String input, int sum, int runningTotal) {
        runningTotal += sum;
        return runningTotal;
    }

    public static void printRow(String input, int runningTotal) {
        int[] charValues = arrayOfDigits(input);
        int sum = sumOfDigits(input);
        System.out.printf("| %-20s | %20s | %20s |%n",input + " (" + charValues[0] + " + " + charValues[1] + " + " + charValues[2] + ")", sum, runningTotal);
        printLines();
    }

    public static ArrayList<String> updateGuessedList(ArrayList<String> guessed, String input) {
        guessed.add(input);
        return guessed;
    }

    public static ArrayList<String> removeWordFromList(ArrayList<String> arr, String input) {
        arr.remove(input);
        return arr;
    }

    public static String getInput(ArrayList<String> guessed, ArrayList<String> arr) {
        char lastWord = getFirstLetter(guessed);
        boolean isValid;
        boolean startsWith;
        String input;
        do {
            input = handleScanner();
            isValid = validateInput(input, arr);
            startsWith = checkIfStartsWith(input, guessed);
            if (!startsWith && isValid) {
                System.out.println("The word doesn't start with " + lastWord + ", please try again");
            }
        } while (!(isValid && startsWith));
        return input;
    }

    public static char getFirstLetter(ArrayList<String> guessed) {
        int lastIndex;
        lastIndex = guessed.size() - 1;
        String lastWord = guessed.get(lastIndex);
        char lastChar = lastWord.charAt(2);
        return lastChar;
    }

//  Check if input starts with last guessed word
    public static boolean checkIfStartsWith(String input, ArrayList<String> guessed) {
        char firstChar = input.charAt(0);
        char firstCharLastWord = getFirstLetter(guessed);
        boolean isValid = true;
        if (!(firstChar == firstCharLastWord)) {
            isValid = false;
        }
        return isValid;
    }

//  Deciding the game winner, if player 1 was last to enter input, boolean value winner is false and player 1 loses.
    static void displayWinner(int winner) {
        int playerLoser = (winner == 1) ? 2 : 1;
        System.out.println("That word takes the total to 200+ ... Player " + winner + " loses.");
        System.out.println("Player " + playerLoser + " wins that game.");

    }

    public static String playFirstRound(ArrayList<String> arr, ArrayList<String> guessed) {
        int runningTotal = 0;
        firstPrompt();
        String input = getFirstInput(arr);
        updateArrays(guessed, arr, input);
        runningTotal += sumOfDigits(input);
        displayTable(guessed, 0, runningTotal);
        return input;
    }

    public static void displayTable(ArrayList<String> guessed, int sum, int runningTotal) {
        displayHeader();
        int runningTotalTemp = 0;
        for (int i = 0; i < guessed.size(); i++) {
            sum = sumOfDigits(guessed.get(i));
            runningTotalTemp += sum;
            runningTotal = runningTotalTemp;
            printRow(guessed.get(i), runningTotalTemp);

        }
    }

    public static void updateArrays(ArrayList<String> guessed, ArrayList<String> arr, String input) {
        updateGuessedList(guessed, input);
        removeWordFromList(arr, input);
    }

    public static int alternatePlayers(int playerNum) {
        return (playerNum == 2) ? 1 : 2;
    }

    public static int playGame() {
        ArrayList<String> arr = readFile(); //     Initialize array to store all the words
        ArrayList<String> guessed = new ArrayList<>();  //      Initialize array to store guessed words
        String input;
        final int THRESHOLD = 200;
        int runningTotal = 0;
        int playerNum = 1;
        int sum = 0;
        playFirstRound(arr,guessed); // Since first round is played differently, call it outside the loop
        while (runningTotal < THRESHOLD) {
            playerNum = alternatePlayers(playerNum);
            char chr = getFirstLetter(guessed);
            displayPrompt(playerNum, chr);
            input = getInput(guessed, arr);
            updateArrays(guessed, arr, input);
            runningTotal += sumOfDigits(input);
            displayTable(guessed, sum, runningTotal);
        }
        return playerNum;
    }

    public static void main(String[] args) {
        int winner = playGame();
        displayWinner(winner);

    }
}
