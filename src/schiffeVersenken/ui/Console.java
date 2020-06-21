/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package schiffeVersenken.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    Scanner keyboard;

    public Console() {
        keyboard = new Scanner(System.in);
    }

    /**
     * Reads the next integer from the command line and returns it
     * @param text Displayed before the user makes the required input
     * @return integer which was input by the user
     */
    public int readIntegerFromStdin(String text) {

        int inputValue = 0;
        boolean invalidInput = true;
        do {

            System.out.print(text);
            try {
                inputValue = keyboard.nextInt();
                invalidInput = false;
            } catch (InputMismatchException ex) {
                System.out.println("Ungültige Zahl. Eingabe Widerholen.");
                keyboard.nextLine();
            }

        } while(invalidInput);

        System.out.println("");
        return inputValue;
    }

    /**
     * Reads the next string from the command line and returns it
     * @param text Displayed before the user makes the required input
     * @return string which was input by the user
     */
    public String readStringFromStdin(String text) {
        String inputString = "";
        boolean invalidInput = true;
        keyboard = new Scanner(System.in);
        do {

            System.out.print(text);
            try {
                inputString = keyboard.nextLine();
                invalidInput = false;
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input. Please provide a proper String.");
            }

        } while(invalidInput);

        System.out.println("");
        return inputString;
    }

}