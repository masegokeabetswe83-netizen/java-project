/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package javaapplication5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
 
/**
 * Unit tests for StorageJSON.
 * Every test uses a temporary folder, so no real files are touched.
 */
public class StorageJSONIT {
 
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
 
    private String filePath;
 
    @Before
    public void setUp() {
        // Path to a file that does not exist yet
        filePath = new File(tempFolder.getRoot(), "messages.json").getAbsolutePath();
    }
 
    /** Writes raw text to a file so tests can set up odd or invalid content. */
    private void writeRaw(String path, String content) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }
 
    // ---------------------------------------------------------------
    // Constant
    // ---------------------------------------------------------------
 
    @Test
    public void testDefaultFileConstant() {
        assertEquals("storedMessages.json", StorageJSON.DEFAULT_FILE);
    }
 
    // ---------------------------------------------------------------
    // storeMessage
    // ---------------------------------------------------------------
 
    @Test
    public void testStoreMessageCreatesFileAndReturnsTrue() {
        boolean result = StorageJSON.storeMessage("1001", "00:1:HELLO", "+27111", "Hello", filePath);
        assertTrue(result);
        assertTrue(new File(filePath).exists());
    }
 
    @Test
    public void testStoreMessageContentCanBeReadBack() {
        StorageJSON.storeMessage("1001", "00:1:HELLO", "+27111", "Hello", filePath);
        String[][] rows = StorageJSON.readStoredMessages(filePath);
 
        assertEquals(1, rows.length);
        assertArrayEquals(new String[]{"1001", "00:1:HELLO", "+27111", "Hello"}, rows[0]);
    }
 
    @Test
    public void testStoreMessageAppendsToExistingFile() {
        StorageJSON.storeMessage("1001", "h1", "+27111", "First", filePath);
        StorageJSON.storeMessage("1002", "h2", "+27222", "Second", filePath);
        StorageJSON.storeMessage("1003", "h3", "+27333", "Third", filePath);
 
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertEquals(3, rows.length);
        assertEquals("First", rows[0][3]);
        assertEquals("Second", rows[1][3]);
        assertEquals("Third", rows[2][3]);
    }
 
    @Test
    public void testStoreMessageReturnsFalseForInvalidPath() {
        String badPath = new File(tempFolder.getRoot(), "no/such/folder/messages.json").getAbsolutePath();
        assertFalse(StorageJSON.storeMessage("1", "h", "+27111", "Hi", badPath));
    }
 
    @Test
    public void testStoreMessageOnMalformedFileStartsFresh() throws IOException {
        writeRaw(filePath, "this is not json {{{");
        boolean result = StorageJSON.storeMessage("1", "h1", "+27111", "Recovered", filePath);
 
        assertTrue(result);
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertEquals(1, rows.length);
        assertEquals("Recovered", rows[0][3]);
    }
 
    @Test
    public void testStoreMessageOnNonArrayJsonStartsFresh() throws IOException {
        // Valid JSON, but an object rather than an array
        writeRaw(filePath, "{\"messageID\":\"old\"}");
        boolean result = StorageJSON.storeMessage("1", "h1", "+27111", "New", filePath);
 
        assertTrue(result);
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertEquals(1, rows.length);
        assertEquals("1", rows[0][0]);
    }
 
    @Test
    public void testStoreMessageSpecialCharactersRoundTrip() {
        String tricky = "He said \"hi\"\nNew line, tab\t, backslash \\ and slash /";
        StorageJSON.storeMessage("1", "h1", "+27111", tricky, filePath);
 
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertEquals(tricky, rows[0][3]);
    }
 
    // ---------------------------------------------------------------
    // overwriteMessages
    // ---------------------------------------------------------------
 
    @Test
    public void testOverwriteMessagesReturnsTrueAndWritesRows() {
        String[][] rows = {
            {"1", "h1", "+27111", "One"},
            {"2", "h2", "+27222", "Two"}
        };
        assertTrue(StorageJSON.overwriteMessages(rows, filePath));
 
        String[][] read = StorageJSON.readStoredMessages(filePath);
        assertEquals(2, read.length);
        assertArrayEquals(rows[0], read[0]);
        assertArrayEquals(rows[1], read[1]);
    }
 
    @Test
    public void testOverwriteMessagesReplacesExistingContent() {
        StorageJSON.storeMessage("old1", "oh1", "+27000", "Old one", filePath);
        StorageJSON.storeMessage("old2", "oh2", "+27000", "Old two", filePath);
 
        String[][] rows = {{"new1", "nh1", "+27999", "New one"}};
        assertTrue(StorageJSON.overwriteMessages(rows, filePath));
 
        String[][] read = StorageJSON.readStoredMessages(filePath);
        assertEquals(1, read.length);
        assertEquals("new1", read[0][0]);
    }
 
    @Test
    public void testOverwriteMessagesWithEmptyArrayClearsFile() {
        StorageJSON.storeMessage("1", "h1", "+27111", "Hi", filePath);
 
        assertTrue(StorageJSON.overwriteMessages(new String[0][4], filePath));
        assertEquals(0, StorageJSON.readStoredMessages(filePath).length);
    }
 
    @Test
    public void testOverwriteMessagesReturnsFalseForInvalidPath() {
        String badPath = new File(tempFolder.getRoot(), "no/such/folder/messages.json").getAbsolutePath();
        String[][] rows = {{"1", "h1", "+27111", "Hi"}};
        assertFalse(StorageJSON.overwriteMessages(rows, badPath));
    }
 
    @Test
    public void testOverwriteMessagesPreservesOrder() {
        String[][] rows = {
            {"3", "h3", "+27333", "C"},
            {"1", "h1", "+27111", "A"},
            {"2", "h2", "+27222", "B"}
        };
        StorageJSON.overwriteMessages(rows, filePath);
 
        String[][] read = StorageJSON.readStoredMessages(filePath);
        assertEquals("3", read[0][0]);
        assertEquals("1", read[1][0]);
        assertEquals("2", read[2][0]);
    }
 
    // ---------------------------------------------------------------
    // readStoredMessages
    // ---------------------------------------------------------------
 
    @Test
    public void testReadStoredMessagesFileDoesNotExist() {
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertNotNull(rows);
        assertEquals(0, rows.length);
    }
 
    @Test
    public void testReadStoredMessagesEmptyArrayFile() throws IOException {
        writeRaw(filePath, "[]");
        assertEquals(0, StorageJSON.readStoredMessages(filePath).length);
    }
 
    @Test
    public void testReadStoredMessagesMalformedFileReturnsEmpty() throws IOException {
        writeRaw(filePath, "[{\"messageID\": \"1\", ");
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertNotNull(rows);
        assertEquals(0, rows.length);
    }
 
    @Test
    public void testReadStoredMessagesEmptyFileReturnsEmpty() throws IOException {
        writeRaw(filePath, "");
        assertEquals(0, StorageJSON.readStoredMessages(filePath).length);
    }
 
    @Test
    public void testReadStoredMessagesNonArrayJsonReturnsEmpty() throws IOException {
        writeRaw(filePath, "{\"messageID\":\"1\"}");
        assertEquals(0, StorageJSON.readStoredMessages(filePath).length);
    }
 
    @Test
    public void testReadStoredMessagesMissingFieldsDefaultToEmptyString() throws IOException {
        writeRaw(filePath, "[{\"messageID\":\"1\"}]");
        String[][] rows = StorageJSON.readStoredMessages(filePath);
 
        assertEquals(1, rows.length);
        assertArrayEquals(new String[]{"1", "", "", ""}, rows[0]);
    }
 
    @Test
    public void testReadStoredMessagesNullValuesDefaultToEmptyString() throws IOException {
        writeRaw(filePath, "[{\"messageID\":null,\"messageHash\":null,"
                + "\"recipient\":null,\"message\":null}]");
        String[][] rows = StorageJSON.readStoredMessages(filePath);
 
        assertArrayEquals(new String[]{"", "", "", ""}, rows[0]);
    }
 
    @Test
    public void testReadStoredMessagesNumericValuesConvertedToString() throws IOException {
        writeRaw(filePath, "[{\"messageID\":42,\"messageHash\":\"h\","
                + "\"recipient\":\"+27111\",\"message\":\"Hi\"}]");
        String[][] rows = StorageJSON.readStoredMessages(filePath);
 
        assertEquals("42", rows[0][0]);
    }
 
    @Test
    public void testReadStoredMessagesReturnsFourColumnsPerRow() {
        StorageJSON.storeMessage("1", "h1", "+27111", "Hi", filePath);
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        assertEquals(4, rows[0].length);
    }
}
 