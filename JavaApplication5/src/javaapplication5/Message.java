/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;

/**
 *
 * @author maseg
 */
public class Message {
    
    // Attributes
    private String messageID;
    private String messageNumber;
    private String recipientCell;
    private String messageContent;
    private String messageHash;
    private String messageStatus;
    
    //Array list
    private static int totalMessagesSent = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
        
    //Constuctor
    public Message(String messageNumber, String recipientCell, String messageContent) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        totalMessagesSent++;
    }
    // Helper method to generate a random 10 digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }
    
    // 1. checkMessageID() -Ensures message ID is not more than 10 character
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    //2. checkRecipientCell()
    public String checkRecipientCell(){
        if (recipientCell != null && recipientCell.matches("^\\+27[1-9][0-9]{8}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    //3. Check Message Length (Max 250 characters)
    public String checkMessageLength() {
        if (messageContent != null && messageContent.length() <= 250) {
            return "Message ready to send.";
        } else{
            int excess = this.messageContent.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }
   
    //Create message hash
    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageContent == null || messageContent.trim().isEmpty()){
            return "";
        }
        
        String firstTwoID = messageID.substring(0, 2);
        
        String[] words = messageContent.trim().split("\\s+");
        
        String firstWord = "";
        String lastWord = ""; 
        
        if (words.length > 0) {
            firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        }
        
        if (words.length > 1){
            lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]","");
        } else{
            lastWord = firstWord;
        }
        
        return (firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
    
    //Sent handler
    public String SentMessages(Scanner input){
        System.out.println("\n--- SEND MESSAGE ---");
        System.out.println("1) Send message");
        System.out.println("2) Disregarded Mesaage");
        System.out.println("3) Store Message");
        System.out.println("Choose an option (1-3): ");
        
        String option = input.nextLine();
    
        switch (option) {
            case "1":
                messageStatus = "Sent";
                sentMessages.add(this);
                return "Message successfully sent.";
                
            case "2":
                messageStatus = "Disregarded";
                disregardedMessages.add(this);
                return "Press 2 to delete the message.";
            case "3":
                messageStatus = "Stored";
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }
    
    //Print messages
    public static String printMessages() {
            StringBuilder output = new StringBuilder();
            if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
                return "No messages have been stored or sent yet.";
            }
            
            output.append("--- Sent Messages ---\n");
            for (Message currentMessage : sentMessages) {
                output.append("ID: ").append(currentMessage.messageID).append("\n");
                output.append("Hash: ").append(currentMessage.messageHash).append("\n");
                output.append("Recipient: ").append(currentMessage.recipientCell).append("\n");
                output.append("Message: ").append(currentMessage.messageContent).append("\n");
                output.append("-------------------------------\n");
            }
            
            output.append("--- Stored Messages ---\n");
            for (Message currentMessage : storedMessages) {
                output.append("ID: ").append(currentMessage.messageID).append("\n");
                output.append("Hash: ").append(currentMessage.messageHash).append("\n");
                output.append("Recipient: ").append(currentMessage.recipientCell).append("\n");
                output.append("Message: ").append(currentMessage.messageContent).append("\n");
                output.append("-------------------------------\n");
            }
            
            return output.toString();
        }
            
            // Total messages
            public static int returnTotalMessages() {
                return totalMessagesSent;
            }
            
            // Store message to Json
            public void storeMessage() {
                String filename = "messages.json";
                
                try (FileWriter writer = new FileWriter(filename, true)){
                    writer.write("{\n");
                    writer.write(" \"messagesID\": \"" + messageID + "\",\n");
                    writer.write(" \"messageNumber\": \"" + messageNumber + "\",\n");
                    writer.write(" \"recipient\": \"" + escapeJson(recipientCell) + "\",\n");
                    writer.write(" \"messageContent\": \"" + escapeJson(messageContent) + "\",n");
                    writer.write(" \"messageHash\": \"" + messageHash + "\",\n");
                    writer.write(" \"messageStatus\": \"" + messageStatus + "\"\n");
                    writer.write("},\n");
                } catch (IOException e){
                    System.out.println("Error storing message: " + e.getMessage());
                }
            }
            
            // Helper: Escape Json
            private String escapeJson(String text) {
                if (text == null) return "";
                return text.replace("\"", "\\\"").replace("\n", "\\n");
            }
           
    // Getters
    public String getMessageID(){
        return messageID;
    }
    
    public String getRecipientCell(){
        return recipientCell;
    }
    
    public String getMessageContent(){
        return messageContent;
    }
    
    public String getMessageHash(){
        return messageHash;
    }
    
    public String getMessageStatus(){
        return messageStatus; 
}
}
