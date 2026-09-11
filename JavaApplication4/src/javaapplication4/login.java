/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author maseg
 */
public class login {
private String storedUsername;
    private String storedPassword;
    private String storedCellPhone;
    private String firstName;
    private String lastName;
    
    //username check method
    public boolean checkUserName(String username) {
        boolean hasUnderscore = username.contains("_");
        boolean hasLength = username.length() <= 5;


        return hasUnderscore && hasLength;
    } 
    
    
    //password check method
    public boolean checkPasswordComplexity(String password) {
        boolean longEnough = password.length() >=8;
        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
        
        for (int i = 0; i < password.length(); i++) {
            char currentChar = password.charAt(i);
            
            if (Character.isUpperCase(currentChar)){
                hasCapitalLetter = true;
            }
            
            if (Character.isDigit(currentChar)){
                hasNumber = true;
            }
            
            if (!Character.isLetterOrDigit(currentChar)){
                hasSpecialCharacter = true;
            }
        }
        
        return longEnough && hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }
   
    
    //cellphone check method
    public boolean checkCellPhoneNumber(String cellNumber) {
        String regexPattern = "^\\+\\d{10,11}$";
        
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(cellNumber);
        
        return matcher.matches();
    }
    
    //register user method
    public String registerUser(String username, String password, String cellNumber,
                                String first, String last) {

        String strMessage;

        if (checkUserName(username) == false) {
            strMessage = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        } else if (checkPasswordComplexity(password) == false) {
            strMessage = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else if (checkCellPhoneNumber(cellNumber) == false) {
            strMessage = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        } else {
            storedUsername = username;
            storedPassword = password;
            storedCellPhone = cellNumber;
            firstName = first;
            lastName = last;
            strMessage = "Username successfully captured.";
        }

        return strMessage;
    }
    
    
    //login user method
    public boolean loginUser(String username, String password) {
        boolean result;

        if (storedUsername == null) {
            result = false;
        } else if (storedUsername.equals(username) && storedPassword.equals(password)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
    
    //login status message
    public String returnLoginStatus(boolean LoginSuccess) {
        String status;

        if (LoginSuccess == true) {
            status = "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            status = "Username or password incorrect, please try again.";
        }

        return status;
    }
}
