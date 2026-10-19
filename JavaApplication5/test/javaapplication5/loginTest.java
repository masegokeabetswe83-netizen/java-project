/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnitTestClass.java to edit this template
 */
package javaapplication5;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maseg
 */
public class loginTest {

    public loginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkUserName method, of class login.
     */
    @Test
    public void testCheckUserName() {
        login instance = new login();
        // Valid username test
        boolean validResult = instance.checkUserName("kyl_1");
        assertTrue(validResult);

        // Test invalid username
        boolean invalidResult = instance.checkUserName("kyle!!!!!!!");
        assertFalse(invalidResult);
    }

    /**
     * Test of checkPasswordComplexity method, of class login.
     */
    @Test
    public void testCheckPasswordComplexity() {
        login instance = new login();

        // Test valid password complexity
        String validPassword = "Ch&&sec@ke99!";
        boolean validResult = instance.checkPasswordComplexity(validPassword);
        assertTrue(validResult);

        // Test invalid password complexity
        String invalidPassword = "password";
        boolean invalidResult = instance.checkPasswordComplexity(invalidPassword);
        assertFalse(invalidResult);

        String shortPassword = "A1t";
        boolean shortResult = instance.checkPasswordComplexity(shortPassword);
        assertFalse(shortResult);
    }

    /**
     * Test of checkCellPhoneNumber method, of class login.
     */
    @Test
    public void testCheckCellPhoneNumber() {
        login instance = new login();
        // Valid phone number
        boolean validResult = instance.checkCellPhoneNumber("+27838968976");
        assertTrue(validResult);
        // Invalid phone number
        boolean invalidResult = instance.checkCellPhoneNumber("08966553");
        assertFalse(invalidResult);
    }

    /**
     * Test of registerUser method, of class login.
     */
    @Test
    public void testRegisterUser() {
        System.out.println("registerUser");
        login instance = new login();

        // Valid registration data
        String username = "kyl_1";
        String password = "Ch&&sec@ke99!";
        String cellNumber = "+27838968976";
        String first = "Kyle";
        String last = "Naidoo";

        String expResult = "Username successfully captured.";
        String result = instance.registerUser(username, password, cellNumber, first, last);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class login, with an invalid username.
     */
    @Test
    public void testRegisterUser_InvalidUsername() {
        System.out.println("registerUser - invalid username");
        login instance = new login();

        // Invalid username (no underscore, too long); password and cell are valid
        String username = "kyle!!!!!!!";
        String password = "Ch&&sec@ke99!";
        String cellNumber = "+27838968976";
        String first = "Kyle";
        String last = "Naidoo";

        String expResult = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        String result = instance.registerUser(username, password, cellNumber, first, last);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class login, with an invalid password.
     */
    @Test
    public void testRegisterUser_InvalidPassword() {
        System.out.println("registerUser - invalid password");
        login instance = new login();

        // Valid username, weak password, valid cell number
        String username = "kyl_1";
        String password = "password";
        String cellNumber = "+27838968976";
        String first = "Kyle";
        String last = "Naidoo";

        String expResult = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String result = instance.registerUser(username, password, cellNumber, first, last);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class login, with an invalid cell number.
     */
    @Test
    public void testRegisterUser_InvalidCellNumber() {
        System.out.println("registerUser - invalid cell number");
        login instance = new login();

        // Valid username, valid password, invalid cell number
        String username = "kyl_1";
        String password = "Ch&&sec@ke99!";
        String cellNumber = "08966553";
        String first = "Kyle";
        String last = "Naidoo";

        String expResult = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        String result = instance.registerUser(username, password, cellNumber, first, last);
        assertEquals(expResult, result);
    }

    /**
     * Test of loginUser method, of class login.
     */
    @Test
    public void testLoginUser() {
        System.out.println("loginUser");
        login instance = new login();

        // Register a user first so there is a valid account to log into
        instance.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Naidoo");

        // Valid credentials
        String username = "kyl_1";
        String password = "Ch&&sec@ke99!";
        boolean expResult = true;
        boolean result = instance.loginUser(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of loginUser method, of class login, with invalid credentials.
     */
    @Test
    public void testLoginUser_InvalidCredentials() {
        System.out.println("loginUser - invalid credentials");
        login instance = new login();

        instance.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Naidoo");

        // Wrong password
        String username = "kyl_1";
        String password = "WrongPassword1!";
        boolean expResult = false;
        boolean result = instance.loginUser(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of returnLoginStatus method, of class login.
     */
    @Test
    public void testReturnLoginStatus() {
        System.out.println("returnLoginStatus");
        login instance = new login();

        // Register first so firstName/lastName are populated
        instance.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Naidoo");

        // Successful login status
        boolean loginSuccess = true;
        String expResult = "Welcome Kyle, Naidoo it is great to see you again.";
        String result = instance.returnLoginStatus(loginSuccess);
        assertEquals(expResult, result);
    }

    /**
     * Test of returnLoginStatus method, of class login, for a failed login.
     */
    @Test
    public void testReturnLoginStatus_Failure() {
        System.out.println("returnLoginStatus - failure");
        login instance = new login();

        // Failed login status
        boolean loginSuccess = false;
        String expResult = "Username or password incorrect, please try again.";
        String result = instance.returnLoginStatus(loginSuccess);
        assertEquals(expResult, result);
    }

}