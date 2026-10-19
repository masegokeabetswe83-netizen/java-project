package javaapplication5;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

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
public class MessageTest {
    
    public MessageTest() {
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
     *  Test Messages Sent
     */
    @Test
    public void testCheckRecipientCellSuccess() {
        Message msg = new Message("1", "+27718693002", "Test content content");
        String result = msg.checkRecipientCell();
        assertEquals("Cell phone number successfully captured.", result);
    }
    
    @Test
    public void testCheckRecipientCellFailure(){
        // Pass an invalid cell nuber format
        Message msg = new Message("1", "12345", "Test message content");
        String result = msg.checkRecipientCell();
        
        assertNotNull(result);
        assertTrue(result.contains("format") || result.contains("invalid") || result.contains("incorrect")); 
    }
    
    @Test
    public void testCheckMessageID() {
        Message msg = new Message("1", "+27718693002", "Hello world");
        boolean isValid = msg.checkMessageID();
        assertTrue(isValid);
    }
    
    @Test
    public void testCheckMessageLengthSuccess(){
        Message msg = new Message("1", "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String result = msg.checkMessageLength();
        assertEquals("Message ready to send.", result);
    }
    
    @Test
    public void testCheckMessageLengthFailure() {
        // create string that exceeds 250 characters 
        String longMessage = "This message is intentionally made very long. ".repeat(7);
        
        Message msg = new Message("1", "+27718693002", longMessage);
        String result = msg.checkMessageLength();
        
        assertNotNull(result);
        
        assertTrue(result.contains("exceeds 250 characters"));
    }
    
    @Test
    public void testCreateMessageHash(){
        //Test that hash is successfully generated and not empty
        Message msg = new Message("1", "+27718693002", "Quick test message");
        String hash = msg.createMessageHash();
        
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertTrue(hash.contains(":"));
    }
    
    @Test
    public void testSentMessageOptions(){
        
        Message msg = new Message("1", "+27718693002", "Action test");
        
        assertEquals("Message successfully sent.", msg.SentMessages(new java.util.Scanner("1\n")));
        assertEquals("Press 2 to delete the message.", msg.SentMessages(new java.util.Scanner("2\n")));
        assertEquals("Message successfully stored.", msg.SentMessages(new java.util.Scanner("3\n")));
        assertEquals("Invalid choice.", msg.SentMessages(new java.util.Scanner("99\n")));
    }
    
    @Test
    public void testReturnTotalMessages() {
        int initialTotal = Message.returnTotalMessages();
        Message msg = new Message("1", "+27718693002", "Count test");
        msg.checkMessageLength();
        
        assertEquals(initialTotal + 1, Message.returnTotalMessages());
    }
    
    @Test
    public void testGetMessagetails() {
        Message msg = new Message("1", "+27718693002", "Hello World");
        
       msg.SentMessages(new java.util.Scanner("1\n"));
        String details = Message.printMessages();
        
        assertNotNull(details);
        assertTrue(details.contains("ID:"));
        assertTrue(details.contains("Hash:"));
    }
}
