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
        
        
}
