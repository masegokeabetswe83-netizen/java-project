 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

import java.util.Scanner;

/**
 *
 * @author maseg
 */
public class SendMessages {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 2. The application must display the required welcome message
        System.out.println("Welcome to QuickChat.");
        boolean running =true;
        
        
        System.out.println("==================================");
        System.out.println("    WELCOME TO QUICKCHAT          ");
        System.out.println("==================================");
        
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1) Send Message");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("Please choose an option (1-3): ");
            
            String choiceInput = scanner.nextLine();
            int choice;
            
            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                continue;
            }
            
            switch (choice) {
            case 1:
                // Asks how many messages user wishes to enter
            System.out.println("How many messages do you want to enter? ");
            int numMessages;
            
            try{
                numMessages = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Invalid number format. Returning to maun menu.");
                break;
            }
            
            for (int i = 1; i <= numMessages; i++){
                System.out.println("\n--- Entering Message " + i + " of " + numMessages + " ---");
                String messageNumberString = String.valueOf (i);
                
                System.out.println("Enter recipient cell number (must include international code): ");
                String recipient = scanner.nextLine();
                
                System.out.println("Enter a message content (max 250 character): ");
                String content = scanner.nextLine();
                
                //Create message object using message class
                Message msg = new Message(String.valueOf(i), recipient, content);
                
                //Run validations
                System.out.println(msg.checkRecipientCell());
                System.out.println(msg.checkMessageLength());
                                
                // Display generated ID and Hash
                System.out.println("Message ID generated: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.createMessageHash());
                
                String actionResult = msg.SentMessages(scanner);
                System.out.println(actionResult);
            }
            case 2:
                // Display sent messages 
                System.out.println("\n--- SENT MESSAGES ---");
                System.out.println(Message.printMessages());
                break;
                
            case 3:
                // Quit the application
                System.out.println("Exiting QuickChat. Goodbye!");
                running = false;
                break;
                
            default:
                System.out.println("Invalid choice. Please choose a number between 1,2 or 3.");    
          }
        }
        scanner.close();
    }
}


