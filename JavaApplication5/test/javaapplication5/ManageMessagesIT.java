/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package javaapplication5;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Unit tests for ManageMessages.
 */
public class ManageMessagesIT {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private ManageMessages manager;

    @Before
    public void setUp() {
        manager = new ManageMessages();
    }

    /** Adds a standard set of four messages: 2 Sent, 1 Stored, 1 Disregard. */
    private void loadSampleData() {
        manager.addMessage("1001", "00:1:HELLOTHERE", "+27834557896", "Hello there", ManageMessages.FLAG_SENT);
        manager.addMessage("1002", "01:2:SEEYOUSOON", "+27838884567", "See you soon at the venue", ManageMessages.FLAG_STORED);
        manager.addMessage("1003", "02:3:IGNOREME", "+27834557896", "Ignore me", ManageMessages.FLAG_DISREGARD);
        manager.addMessage("1004", "03:4:OKAYBYE", "+27830001111", "Ok bye", ManageMessages.FLAG_SENT);
    }

    // ---------------------------------------------------------------
    // addMessage
    // ---------------------------------------------------------------

    @Test
    public void testAddMessageReturnsTrueAndIncrementsCount() {
        boolean added = manager.addMessage("1", "h1", "+27111", "Hi", ManageMessages.FLAG_SENT);
        assertTrue(added);
        assertEquals(1, manager.getMessageCount());
    }

    @Test
    public void testAddMessagePopulatesCorrectFlagArrays() {
        loadSampleData();
        assertEquals(2, manager.getSentMessages().length);
        assertEquals(1, manager.getStoredMessages().length);
        assertEquals(1, manager.getDisregardedMessages().length);
        assertEquals(4, manager.getMessageCount());
    }

    @Test
    public void testAddMessageUnknownFlagCountsButNotInFlagArrays() {
        boolean added = manager.addMessage("1", "h1", "+27111", "Hi", "Unknown");
        assertTrue(added);
        assertEquals(1, manager.getMessageCount());
        assertEquals(0, manager.getSentMessages().length);
        assertEquals(0, manager.getStoredMessages().length);
        assertEquals(0, manager.getDisregardedMessages().length);
    }

    @Test
    public void testAddMessageReturnsFalseWhenFull() {
        for (int i = 0; i < 100; i++) {
            assertTrue(manager.addMessage("id" + i, "hash" + i, "+27111", "msg" + i, ManageMessages.FLAG_SENT));
        }
        boolean added = manager.addMessage("extra", "extraHash", "+27111", "overflow", ManageMessages.FLAG_SENT);
        assertFalse(added);
        assertEquals(100, manager.getMessageCount());
    }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------

    @Test
    public void testGetSentMessagesContent() {
        loadSampleData();
        assertArrayEquals(new String[]{"Hello there", "Ok bye"}, manager.getSentMessages());
    }

    @Test
    public void testGetStoredMessagesContent() {
        loadSampleData();
        assertArrayEquals(new String[]{"See you soon at the venue"}, manager.getStoredMessages());
    }

    @Test
    public void testGetDisregardedMessagesContent() {
        loadSampleData();
        assertArrayEquals(new String[]{"Ignore me"}, manager.getDisregardedMessages());
    }

    @Test
    public void testGetMessageIDs() {
        loadSampleData();
        assertArrayEquals(new String[]{"1001", "1002", "1003", "1004"}, manager.getMessageIDs());
    }

    @Test
    public void testGetMessageHashes() {
        loadSampleData();
        assertArrayEquals(new String[]{"00:1:HELLOTHERE", "01:2:SEEYOUSOON",
            "02:3:IGNOREME", "03:4:OKAYBYE"}, manager.getMessageHashes());
    }

    @Test
    public void testGettersEmptyManager() {
        assertEquals(0, manager.getMessageCount());
        assertEquals(0, manager.getSentMessages().length);
        assertEquals(0, manager.getStoredMessages().length);
        assertEquals(0, manager.getDisregardedMessages().length);
        assertEquals(0, manager.getMessageIDs().length);
        assertEquals(0, manager.getMessageHashes().length);
    }

    // ---------------------------------------------------------------
    // displayMessageDetailsByID / searchByMessageID
    // ---------------------------------------------------------------

    @Test
    public void testDisplayMessageDetailsByIDFound() {
        loadSampleData();
        String expected = "Recipient: +27838884567\nMessage: See you soon at the venue";
        assertEquals(expected, manager.displayMessageDetailsByID("1002"));
    }

    @Test
    public void testDisplayMessageDetailsByIDNotFound() {
        loadSampleData();
        assertEquals("Message ID not found.", manager.displayMessageDetailsByID("9999"));
    }

    @Test
    public void testSearchByMessageIDFound() {
        loadSampleData();
        assertEquals("Hello there", manager.searchByMessageID("1001"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        loadSampleData();
        assertEquals("Message ID not found.", manager.searchByMessageID("nope"));
    }

    @Test
    public void testSearchByMessageIDEmptyManager() {
        assertEquals("Message ID not found.", manager.searchByMessageID("1001"));
    }

    // ---------------------------------------------------------------
    // displaySenderAndRecipient
    // ---------------------------------------------------------------

    @Test
    public void testDisplaySenderAndRecipientStoredOnly() {
        loadSampleData();
        String result = manager.displaySenderAndRecipient("Alice");
        assertEquals("Sender: Alice | Recipient: +27838884567\n", result);
    }

    @Test
    public void testDisplaySenderAndRecipientMultipleStored() {
        manager.addMessage("1", "h1", "+27111", "A", ManageMessages.FLAG_STORED);
        manager.addMessage("2", "h2", "+27222", "B", ManageMessages.FLAG_STORED);
        String result = manager.displaySenderAndRecipient("Bob");
        assertEquals("Sender: Bob | Recipient: +27111\n"
                + "Sender: Bob | Recipient: +27222\n", result);
    }

    @Test
    public void testDisplaySenderAndRecipientNoStored() {
        manager.addMessage("1", "h1", "+27111", "A", ManageMessages.FLAG_SENT);
        assertEquals("No stored messages.", manager.displaySenderAndRecipient("Alice"));
    }

    // ---------------------------------------------------------------
    // findLongestStoredMessage
    // ---------------------------------------------------------------

    @Test
    public void testFindLongestStoredMessage() {
        manager.addMessage("1", "h1", "+27111", "short", ManageMessages.FLAG_STORED);
        manager.addMessage("2", "h2", "+27222", "this one is much longer", ManageMessages.FLAG_STORED);
        manager.addMessage("3", "h3", "+27333", "medium length", ManageMessages.FLAG_STORED);
        assertEquals("this one is much longer", manager.findLongestStoredMessage());
    }

    @Test
    public void testFindLongestStoredMessageIgnoresSentAndDisregard() {
        manager.addMessage("1", "h1", "+27111", "tiny", ManageMessages.FLAG_STORED);
        manager.addMessage("2", "h2", "+27222", "a very long sent message indeed", ManageMessages.FLAG_SENT);
        manager.addMessage("3", "h3", "+27333", "a very long disregarded message indeed", ManageMessages.FLAG_DISREGARD);
        assertEquals("tiny", manager.findLongestStoredMessage());
    }

    @Test
    public void testFindLongestStoredMessageNoneStored() {
        manager.addMessage("1", "h1", "+27111", "sent only", ManageMessages.FLAG_SENT);
        assertEquals("", manager.findLongestStoredMessage());
    }

    @Test
    public void testFindLongestStoredMessageTieReturnsFirst() {
        manager.addMessage("1", "h1", "+27111", "abcd", ManageMessages.FLAG_STORED);
        manager.addMessage("2", "h2", "+27222", "wxyz", ManageMessages.FLAG_STORED);
        assertEquals("abcd", manager.findLongestStoredMessage());
    }

    // ---------------------------------------------------------------
    // searchByRecipient
    // ---------------------------------------------------------------

    @Test
    public void testSearchByRecipientReturnsSentAndStoredOnly() {
        loadSampleData();
        // +27834557896 has one Sent ("Hello there") and one Disregard ("Ignore me")
        String[] result = manager.searchByRecipient("+27834557896");
        assertArrayEquals(new String[]{"Hello there"}, result);
    }

    @Test
    public void testSearchByRecipientMultipleMatches() {
        manager.addMessage("1", "h1", "+27111", "first", ManageMessages.FLAG_SENT);
        manager.addMessage("2", "h2", "+27222", "other", ManageMessages.FLAG_SENT);
        manager.addMessage("3", "h3", "+27111", "second", ManageMessages.FLAG_STORED);
        assertArrayEquals(new String[]{"first", "second"}, manager.searchByRecipient("+27111"));
    }

    @Test
    public void testSearchByRecipientNoMatch() {
        loadSampleData();
        assertEquals(0, manager.searchByRecipient("+27999999999").length);
    }

    @Test
    public void testSearchByRecipientOnlyDisregardedReturnsEmpty() {
        manager.addMessage("1", "h1", "+27111", "ignored", ManageMessages.FLAG_DISREGARD);
        assertEquals(0, manager.searchByRecipient("+27111").length);
    }

    // ---------------------------------------------------------------
    // deleteByHash
    // ---------------------------------------------------------------

    @Test
    public void testDeleteByHashSuccessMessage() {
        loadSampleData();
        String result = manager.deleteByHash("01:2:SEEYOUSOON");
        assertEquals("Message: \"See you soon at the venue\" successfully deleted.", result);
    }

    @Test
    public void testDeleteByHashNotFound() {
        loadSampleData();
        assertEquals("Message hash not found.", manager.deleteByHash("does-not-exist"));
        assertEquals(4, manager.getMessageCount());
    }

    @Test
    public void testDeleteByHashEmptyManager() {
        assertEquals("Message hash not found.", manager.deleteByHash("anything"));
    }

    @Test
    public void testDeleteByHashRemovesFromSentArray() {
        loadSampleData();
        manager.deleteByHash("00:1:HELLOTHERE");
        assertArrayEquals(new String[]{"Ok bye"}, manager.getSentMessages());
        assertEquals(3, manager.getMessageCount());
    }

    @Test
    public void testDeleteByHashRemovesFromStoredArray() {
        loadSampleData();
        manager.deleteByHash("01:2:SEEYOUSOON");
        assertEquals(0, manager.getStoredMessages().length);
        assertEquals(3, manager.getMessageCount());
    }

    @Test
    public void testDeleteByHashRemovesFromDisregardedArray() {
        loadSampleData();
        manager.deleteByHash("02:3:IGNOREME");
        assertEquals(0, manager.getDisregardedMessages().length);
        assertEquals(3, manager.getMessageCount());
    }

    @Test
    public void testDeleteByHashShiftsParallelArraysAndPreservesOrder() {
        loadSampleData();
        manager.deleteByHash("01:2:SEEYOUSOON"); // delete the second message
        assertArrayEquals(new String[]{"1001", "1003", "1004"}, manager.getMessageIDs());
        assertArrayEquals(new String[]{"00:1:HELLOTHERE", "02:3:IGNOREME", "03:4:OKAYBYE"},
                manager.getMessageHashes());
        // Lookups still resolve correctly after the shift
        assertEquals("Ignore me", manager.searchByMessageID("1003"));
        assertEquals("Ok bye", manager.searchByMessageID("1004"));
        assertEquals("Message ID not found.", manager.searchByMessageID("1002"));
    }

    @Test
    public void testDeleteLastMessage() {
        loadSampleData();
        manager.deleteByHash("03:4:OKAYBYE");
        assertArrayEquals(new String[]{"1001", "1002", "1003"}, manager.getMessageIDs());
    }

    @Test
    public void testDeleteAllowsAddingAgainAfterFull() {
        for (int i = 0; i < 100; i++) {
            manager.addMessage("id" + i, "hash" + i, "+27111", "msg" + i, ManageMessages.FLAG_SENT);
        }
        manager.deleteByHash("hash0");
        assertTrue(manager.addMessage("new", "newHash", "+27111", "new msg", ManageMessages.FLAG_SENT));
    }

    @Test
    public void testDeleteSameHashTwiceSecondFails() {
        loadSampleData();
        manager.deleteByHash("00:1:HELLOTHERE");
        assertEquals("Message hash not found.", manager.deleteByHash("00:1:HELLOTHERE"));
    }

    // ---------------------------------------------------------------
    // displayReport
    // ---------------------------------------------------------------

    @Test
    public void testDisplayReportExcludesDisregarded() {
        loadSampleData();
        String report = manager.displayReport();
        assertTrue(report.startsWith("===== MESSAGE REPORT ====="));
        assertTrue(report.contains("1001"));
        assertTrue(report.contains("1002"));
        assertTrue(report.contains("1004"));
        assertTrue(report.contains("Hello there"));
        assertTrue(report.contains("Sent"));
        assertTrue(report.contains("Stored"));
        assertFalse(report.contains("1003"));
        assertFalse(report.contains("Ignore me"));
        assertFalse(report.contains("No messages to report."));
    }

    @Test
    public void testDisplayReportEmpty() {
        String report = manager.displayReport();
        assertTrue(report.contains("No messages to report."));
    }

    @Test
    public void testDisplayReportOnlyDisregardedIsEmpty() {
        manager.addMessage("1", "h1", "+27111", "ignored", ManageMessages.FLAG_DISREGARD);
        assertTrue(manager.displayReport().contains("No messages to report."));
    }

    @Test
    public void testDisplayReportContainsAllFields() {
        manager.addMessage("1001", "00:1:HELLO", "+27111", "Hello", ManageMessages.FLAG_SENT);
        String report = manager.displayReport();
        assertTrue(report.contains("Message ID:   1001"));
        assertTrue(report.contains("Message Hash: 00:1:HELLO"));
        assertTrue(report.contains("Recipient:    +27111"));
        assertTrue(report.contains("Message:      Hello"));
        assertTrue(report.contains("Status:       Sent"));
    }

    // ---------------------------------------------------------------
    // JSON persistence (depends on StorageJSON)
    // ---------------------------------------------------------------

    @Test
    public void testSaveStoredToJsonReturnsTrue() throws IOException {
        loadSampleData();
        File file = tempFolder.newFile("stored.json");
        assertTrue(manager.saveStoredToJson(file.getAbsolutePath()));
    }

    @Test
    public void testSaveAndLoadRoundTrip() throws IOException {
        manager.addMessage("2001", "00:1:HI", "+27111", "Hi there", ManageMessages.FLAG_STORED);
        manager.addMessage("2002", "01:2:BYE", "+27222", "Bye now", ManageMessages.FLAG_STORED);
        manager.addMessage("2003", "02:3:SENT", "+27333", "Already sent", ManageMessages.FLAG_SENT);

        File file = tempFolder.newFile("roundtrip.json");
        assertTrue(manager.saveStoredToJson(file.getAbsolutePath()));

        ManageMessages fresh = new ManageMessages();
        int loaded = fresh.loadStoredFromJson(file.getAbsolutePath());

        assertEquals(2, loaded); // Sent message must not have been saved
        assertEquals(2, fresh.getMessageCount());
        assertArrayEquals(new String[]{"Hi there", "Bye now"}, fresh.getStoredMessages());
        assertEquals("Hi there", fresh.searchByMessageID("2001"));
        assertEquals("Bye now", fresh.searchByMessageID("2002"));
    }

    @Test
    public void testLoadStoredFromJsonSkipsDuplicateIDs() throws IOException {
        manager.addMessage("2001", "00:1:HI", "+27111", "Hi there", ManageMessages.FLAG_STORED);
        File file = tempFolder.newFile("dupes.json");
        assertTrue(manager.saveStoredToJson(file.getAbsolutePath()));

        // Loading into the same manager: the ID already exists, so nothing new is added
        int loaded = manager.loadStoredFromJson(file.getAbsolutePath());
        assertEquals(0, loaded);
        assertEquals(1, manager.getMessageCount());
    }

    @Test
    public void testSaveStoredWithNoStoredMessages() throws IOException {
        manager.addMessage("1", "h1", "+27111", "sent only", ManageMessages.FLAG_SENT);
        File file = tempFolder.newFile("empty.json");
        assertTrue(manager.saveStoredToJson(file.getAbsolutePath()));

        ManageMessages fresh = new ManageMessages();
        assertEquals(0, fresh.loadStoredFromJson(file.getAbsolutePath()));
    }
}