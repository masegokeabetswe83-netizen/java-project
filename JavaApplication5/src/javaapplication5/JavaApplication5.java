/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication5;

import java.util.Scanner;

/**
 *
 * @author maseg
 */
public class JavaApplication5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        login obj = new login();
        boolean registered = false;

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    registered = registerFlow(input, obj);
                    break;
                case "2":
                    if (!registered) {
                        System.out.println("No user is registered yet. Please register first.");
                    } else {
                        loginFlow(input, obj);
                    }
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
    }

    /**
     * Walks the user through registration, validating each field until it
     * passes before moving to the next one.
     */
    private static boolean registerFlow(Scanner input, login obj) {
        // username
        String username;
        while (true) {
            System.out.println("Enter a username (must contain an underscore) and be no more than 5 characters");
            username = input.nextLine();

            if (obj.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        // password
        String password;
        while (true) {
            System.out.println("Enter a password (at least 8 characters; a capital letter, a number and a special character)");
            password = input.nextLine();

            if (obj.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.");
            }
        }

        // cellphone
        String cellNumber;
        while (true) {
            System.out.println("Enter your cell phone number (with international code, e.g. +27838968976)");
            cellNumber = input.nextLine();

            if (obj.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }

        // first and last name
        System.out.println("Enter your first name:");
        String firstName = input.nextLine();

        System.out.println("Enter your last name:");
        String lastName = input.nextLine();

        String registrationMessage = obj.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(registrationMessage);

        // registerUser only stores the details if every check passed, so
        // checking the stored state (via a login attempt below) isn't needed:
        // we already know it succeeded because every field was pre-validated
        // above before registerUser was ever called.
        return true;
    }

    /**
     * Prompts for credentials, giving the user up to three attempts before
     * returning to the menu.
     */
    private static void loginFlow(Scanner input, login obj) {
        final int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("Enter your username:");
            String loginUsername = input.nextLine();

            System.out.println("Enter your password:");
            String loginPassword = input.nextLine();

            boolean loginSuccess = obj.loginUser(loginUsername, loginPassword);
            String loginStatusMessage = obj.returnLoginStatus(loginSuccess);
            System.out.println(loginStatusMessage);

            if (loginSuccess) {
                return;
            }

            int remaining = maxAttempts - attempt;
            if (remaining > 0) {
                System.out.println("You have " + remaining + " attempt(s) remaining.");
            } else {
                System.out.println("No attempts remaining. Returning to menu.");
            }
        }
    }

}