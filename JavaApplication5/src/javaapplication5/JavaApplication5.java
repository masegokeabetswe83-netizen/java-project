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

    private static final String JSON_FILE = "messages.json";
    private static final int MAX_LENGTH = 250;

    // Set during registration, used as the sender name in Part 3
    private static String senderName = "";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        login obj = new login();
        boolean registered = false;
        boolean running = true;

        while (running) {
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
                    } else if (loginFlow(input, obj)) {
                        // Part 3: messaging unlocks only after a successful login
                        runQuickChat(input);
                        running = false;
                    } else {
                        System.out.println("Too many failed attempts. Program ending.");
                        running = false;
                    }
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
        input.close();
    }

    // ==================================================================
    // PART 2: registration and login
    // ==================================================================

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

        // Remember the sender name for the Part 3 sender/recipient display
        senderName = firstName + " " + lastName;

        // Every field was validated above before registerUser was called.
        return true;
    }

    /**
     * Prompts for credentials, giving the user up to three attempts.
     * Returns true on success, false if all attempts fail.
     */
    private static boolean loginFlow(Scanner input, login obj) {
        final int maxAttempts = 3;
        boolean loginSuccess = false;

        for (int attempt = 1; attempt <= maxAttempts && !loginSuccess; attempt++) {
            System.out.println("Enter your username:");
            String loginUsername = input.nextLine();

            System.out.println("Enter your password:");
            String loginPassword = input.nextLine();

            loginSuccess = obj.loginUser(loginUsername, loginPassword);
            String loginStatusMessage = obj.returnLoginStatus(loginSuccess);
            System.out.println(loginStatusMessage);

            if (!loginSuccess) {
                int remaining = maxAttempts - attempt;
                if (remaining > 0) {
                    System.out.println("You have " + remaining + " attempt(s) remaining.");
                } else {
                    System.out.println("No attempts remaining.");
                }
            }
        }
        return loginSuccess;
    }

    // ==================================================================
    // PART 3: QuickChat
    // ==================================================================

    private static void runQuickChat(Scanner input) {
        ManageMessages manager = new ManageMessages();
        int loaded = manager.loadStoredFromJson(JSON_FILE);

        System.out.println("\nWelcome to QuickChat.");
        if (loaded > 0) {
            System.out.println(loaded + " stored message(s) loaded from " + JSON_FILE + ".");
        }

        int numMessages = readInt(input, "How many messages do you wish to enter? ", 1, 100);

        boolean running = true;
        while (running) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int choice = readInt(input, "Choose an option: ", 1, 4);

            switch (choice) {
                case 1:
                    sendMessages(input, manager, numMessages);
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    System.out.println("Total messages sent: " + manager.getSentMessages().length);
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                default:
                    storedMessagesMenu(input, manager);
            }
        }
    }

    // Menu option 1: loop counter = message number used in the hash
    private static void sendMessages(Scanner input, ManageMessages manager, int numMessages) {
        Message message = new Message();

        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            String recipient;
            while (true) {
                System.out.println("Enter the recipient's cell number (with international code):");
                recipient = input.nextLine().trim();
                if (message.checkRecipientCell(recipient)) {
                    break;
                }
                System.out.println("Cell phone number is incorrectly formatted or does not contain an international code.");
            }

            String text;
            while (true) {
                System.out.println("Enter your message (max " + MAX_LENGTH + " characters):");
                text = input.nextLine().trim();
                if (text.isEmpty()) {
                    System.out.println("Message cannot be empty.");
                } else if (text.length() > MAX_LENGTH) {
                    System.out.println("Message exceeds " + MAX_LENGTH + " characters by "
                            + (text.length() - MAX_LENGTH) + ", please reduce size.");
                } else {
                    break;
                }
            }

            String id = message.createMessageID();
            String hash = message.createMessageHash(id, i, text);

            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int action = readInt(input, "Choose an option: ", 1, 3);

            if (action == 1) {
                manager.addMessage(id, hash, recipient, text, ManageMessages.FLAG_SENT);
                System.out.println("Message successfully sent.");
                System.out.println("Message ID:   " + id);
                System.out.println("Message Hash: " + hash);
                System.out.println("Recipient:    " + recipient);
                System.out.println("Message:      " + text);
            } else if (action == 2) {
                manager.addMessage(id, hash, recipient, text, ManageMessages.FLAG_DISREGARD);
                System.out.println("Message disregarded.");
            } else {
                manager.addMessage(id, hash, recipient, text, ManageMessages.FLAG_STORED);
                if (manager.saveStoredToJson(JSON_FILE)) {
                    System.out.println("Message successfully stored.");
                } else {
                    System.out.println("Message kept in memory, but saving to " + JSON_FILE + " failed.");
                }
            }
        }
        System.out.println("\nTotal messages sent: " + manager.getSentMessages().length);
    }

    // Menu option 4: Stored Messages sub-menu (a to f)
    private static void storedMessagesMenu(Scanner input, ManageMessages manager) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Stored Messages ===");
            System.out.println("1) Sender and recipient of all stored messages");
            System.out.println("2) Longest stored message");
            System.out.println("3) Search by message ID");
            System.out.println("4) Search by recipient (sent or stored)");
            System.out.println("5) Delete a message by its hash");
            System.out.println("6) Message report");
            System.out.println("7) Back to main menu");
            int choice = readInt(input, "Choose an option: ", 1, 7);

            switch (choice) {
                case 1:
                    System.out.println(manager.displaySenderAndRecipient(senderName));
                    break;
                case 2:
                    String longest = manager.findLongestStoredMessage();
                    if (longest.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("Longest stored message:\n" + longest);
                    }
                    break;
                case 3:
                    System.out.println("Enter the message ID:");
                    System.out.println(manager.displayMessageDetailsByID(input.nextLine().trim()));
                    break;
                case 4:
                    System.out.println("Enter the recipient's cell number:");
                    String[] found = manager.searchByRecipient(input.nextLine().trim());
                    if (found.length == 0) {
                        System.out.println("No sent or stored messages for that recipient.");
                    } else {
                        for (int i = 0; i < found.length; i++) {
                            System.out.println((i + 1) + ". " + found[i]);
                        }
                    }
                    break;
                case 5:
                    System.out.println("Enter the message hash:");
                    System.out.println(manager.deleteByHash(input.nextLine().trim()));
                    manager.saveStoredToJson(JSON_FILE); // keep the file in step with memory
                    break;
                case 6:
                    System.out.println(manager.displayReport());
                    break;
                default:
                    inMenu = false;
            }
        }
    }

    // Keeps asking until the user enters a whole number between min and max
    private static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
