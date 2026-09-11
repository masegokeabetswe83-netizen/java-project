/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication4;

/**
 *
 * @author maseg
 */
import java.util.Scanner;
public class JavaApplication4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input = new Scanner(System.in);
        login obj = new login();
        
        //username 
        
        while (true) {
            System.out.println("Enter a username (must contain an underscore) and be no more than 5 characters");
            String username = input.nextLine();
            
            if (obj.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscire and is no more than five characters in length.");
            }
        }
        
        //password 
        
        while (true) {
            System.out.println("Enter a password (at least 8 characyers; a capital letter, a number and a special character)");
            String password = input.nextLine();
            
            if (obj.checkPasswordComplexity(password)){
                System.out.println("Password successfully captured");
                break;
            }else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.");
            }
        }
        
        //cellphone 
        
        while (true) {
            System.out.println("Enter your cell phone number (with international code, e.g. +27838968976)");
            String cellNumber = input.nextLine();
            
            if (obj.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            }else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }
        
        //first and last name 
        System.out.println("Enter your first name:");
        String firstName = input.nextLine();
        
        System.out.println("Enter your last name");
        String lastName = input.nextLine();
        
        //register 
        String registerationMessage = obj.registerUser(lastName, lastName, lastName, firstName, lastName);
        System.out.println(registerationMessage);
        
        //login 
        System.out.println("Enter your username:");
        String loginUsername = input.nextLine();
        
        System.out.println("Enter your password");
        String loginPassword = input.nextLine();
        
        String loginStatusMessage = obj.returnLoginStatus(true);
        System.out.println(loginStatusMessage);
        
        input.close();
    }
    
}
