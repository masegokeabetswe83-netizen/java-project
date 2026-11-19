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

    private static final String JSON_FILE = "messages.json";

    // Sender name, set during registration
    private static String senderName = "";

    // How many of the requested messages have been entered so far (carries over between Send runs)
    private static int messagesEntered = 0;

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
                        runQuickChat(input); // messaging unlocks only after a successful login
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
                    break;
            }
        }
        input.close();
    }

    // ==================================================================
    // Registration and login
    // ==================================================================

    /**
     * Walks the user through registration, validating each field until it
     * passes before moving to the next one.
     */
    
    private static boolean registerFlow(Scanner input, login obj) {
        String username;
        while (true) {
            System.out.println("Enter a username (must contain an underscore) and be no more than 5 characters");
            username = input.nextLine();
            if (obj.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        String password;
        while (true) {
            System.out.println("Enter a password (at least 8 characters; a capital letter, a number and a special character)");
            password = input.nextLine();
            if (obj.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured");
                break;
            }
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.");
        }

        String cellNumber;
        while (true) {
            System.out.println("Enter your cell phone number (with international code, e.g. +27838968976)");
            cellNumber = input.nextLine();
            if (obj.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            }
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        System.out.println("Enter your first name:");
        String firstName = input.nextLine();
        System.out.println("Enter your last name:");
        String lastName = input.nextLine();

        System.out.println(obj.registerUser(username, password, cellNumber, firstName, lastName));
        senderName = firstName + " " + lastName;
        return true;
    }

    /** Three attempts. Returns true on success, false if all attempts fail. */
    private static boolean loginFlow(Scanner input, login obj) {
        final int maxAttempts = 3;
        boolean loginSuccess = false;

        for (int attempt = 1; attempt <= maxAttempts && !loginSuccess; attempt++) {
            System.out.println("Enter your username:");
            String loginUsername = input.nextLine();
            System.out.println("Enter your password:");
            String loginPassword = input.nextLine();

            loginSuccess = obj.loginUser(loginUsername, loginPassword);
            System.out.println(obj.returnLoginStatus(loginSuccess));

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
    // QuickChat
    // ==================================================================

    private static void runQuickChat(Scanner input) {
        ManageMessages manager = new ManageMessages();
        int loaded = manager.loadStoredFromJson(JSON_FILE);

        System.out.println("\nWelcome to QuickChat.");
        if (loaded > 0) {
            System.out.println(loaded + " stored message(s) loaded from " + JSON_FILE + ":");
            System.out.println(manager.displayReport());
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
                    break;
            }
        }
    }

    /**
     * Send Messages. The message number is the loop counter, starting at 0.
     * The count of messages entered carries over, so choosing Send again
     * continues the numbering and never gives more than the requested total.
     */
    private static void sendMessages(Scanner input, ManageMessages manager, int numMessages) {
        if (messagesEntered >= numMessages) {
            System.out.println("You have already entered all " + numMessages + " messages.");
            return;
        }

        for (int i = messagesEntered; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
            Message message = readValidMessage(input, i);
            handleMessage(input, manager, message);
            messagesEntered++;
        }
        System.out.println("\nTotal messages sent: " + manager.getSentMessages().length);
    }

    /** Asks for the recipient and text until both are valid. */
    private static Message readValidMessage(Scanner input, int messageNumber) {
        Message message = null;
        while (message == null) {
            System.out.println("Enter the recipient's cell number (with international code):");
            String recipient = input.nextLine().trim();
            System.out.println("Enter your message (max 250 characters):");
            String text = input.nextLine().trim();

            Message candidate = new Message(String.valueOf(messageNumber), recipient, text);
            String cellResult = candidate.checkRecipientCell();
            String lengthResult = candidate.checkMessageLength();

            boolean cellOk = cellResult.startsWith("Cell phone number successfully");
            boolean textOk = !text.isEmpty() && lengthResult.equals("Message ready to send.");

            if (!cellOk) {
                System.out.println(cellResult);
            }
            if (text.isEmpty()) {
                System.out.println("Message cannot be empty.");
            } else if (!textOk) {
                System.out.println(lengthResult);
            }
            if (cellOk && textOk) {
                message = candidate;
            }
        }
        return message;
    }

    /** Send, disregard or store the message, and add it to the right arrays. */
    private static void handleMessage(Scanner input, ManageMessages manager, Message message) {
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int action = readInt(input, "Choose an option: ", 1, 3);

        String flag = ManageMessages.FLAG_STORED;
        if (action == 1) {
            flag = ManageMessages.FLAG_SENT;
        } else if (action == 2) {
            flag = ManageMessages.FLAG_DISREGARD;
        }
        manager.addMessage(message.getMessageID(), message.getMessageHash(),
                message.getRecipientCell(), message.getMessageContent(), flag);

        if (action == 1) {
            System.out.println("Message successfully sent.");
            System.out.println("Message ID:   " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient:    " + message.getRecipientCell());
            System.out.println("Message:      " + message.getMessageContent());
        } else if (action == 2) {
            System.out.println("Message disregarded.");
        } else if (manager.saveStoredToJson(JSON_FILE)) {
            System.out.println("Message successfully stored.");
        } else {
            System.out.println("Message kept in memory, but saving to " + JSON_FILE + " failed.");
        }
    }

    // ==================================================================
    // Stored Messages sub-menu
    // ==================================================================

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
                    showLongest(manager);
                    break;
                case 3:
                    searchById(input, manager);
                    break;
                case 4:
                    searchByRecipient(input, manager);
                    break;
                case 5:
                    deleteByHash(input, manager);
                    break;
                case 6:
                    System.out.println(manager.displayReport());
                    break;
                default:
                    inMenu = false;
                    break;
            }
        }
    }

    private static void showLongest(ManageMessages manager) {
        String longest = manager.findLongestStoredMessage();
        if (longest.isEmpty()) {
            System.out.println("No stored messages.");
        } else {
            System.out.println("Longest stored message:\n" + longest);
        }
    }

    private static void searchById(Scanner input, ManageMessages manager) {
        System.out.println("Enter the message ID:");
        System.out.println(manager.displayMessageDetailsByID(input.nextLine().trim()));
    }

    private static void searchByRecipient(Scanner input, ManageMessages manager) {
        System.out.println("Enter the recipient's cell number:");
        String[] found = manager.searchByRecipient(input.nextLine().trim());
        if (found.length == 0) {
            System.out.println("No sent or stored messages for that recipient.");
        }
        for (int i = 0; i < found.length; i++) {
            System.out.println((i + 1) + ". " + found[i]);
        }
    }

    private static void deleteByHash(Scanner input, ManageMessages manager) {
        System.out.println("Enter the message hash:");
        System.out.println(manager.deleteByHash(input.nextLine().trim()));
        manager.saveStoredToJson(JSON_FILE); // keep the file in step with memory
    }

    /** Keeps asking until the user enters a whole number between min and max. */
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













