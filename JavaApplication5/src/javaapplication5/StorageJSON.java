/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

/**
 *
 * @author maseg
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class StorageJSON {
    public static final String DEFAULT_FILE = "storedMessages.json";
    
    @SuppressWarnings("unchecked")
    public static boolean storeMessage(String messageID, String messageHash,
            String recipient, String message, String filePath) {
        JSONArray messageArray = readJsonArray(filePath);

        JSONObject messageObject = new JSONObject();
        messageObject.put("messageID", messageID);
        messageObject.put("messageHash", messageHash);
        messageObject.put("recipient", recipient);
        messageObject.put("message", message);
        messageArray.add(messageObject);

        return writeJsonArray(messageArray, filePath);
    }
    
    @SuppressWarnings("unchecked")
    public static boolean overwriteMessages(String[][] rows, String filePath) {
        JSONArray messageArray = new JSONArray();
        int index = 0;
        while (index < rows.length) {
            JSONObject messageObject = new JSONObject();
            messageObject.put("messageID", rows[index][0]);
            messageObject.put("messageHash", rows[index][1]);
            messageObject.put("recipient", rows[index][2]);
            messageObject.put("message", rows[index][3]);
            messageArray.add(messageObject);
            index++;
        }
        return writeJsonArray(messageArray, filePath);
    }
    
    public static String[][] readStoredMessages(String filePath) {
        JSONArray messageArray = readJsonArray(filePath);
        String[][] rows = new String[messageArray.size()][4];

        int index = 0;
        while (index < messageArray.size()) {
            JSONObject messageObject = (JSONObject) messageArray.get(index);
            rows[index][0] = getField(messageObject, "messageID");
            rows[index][1] = getField(messageObject, "messageHash");
            rows[index][2] = getField(messageObject, "recipient");
            rows[index][3] = getField(messageObject, "message");
            index++;
        }
        return rows;
    }
    
    // Returns the existing array from the file, or an empty one if there is none
    private static JSONArray readJsonArray(String filePath) {
        JSONArray messageArray = new JSONArray();
        File file = new File(filePath);

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Object parsed = new JSONParser().parse(reader);
                if (parsed instanceof JSONArray) {
                    messageArray = (JSONArray) parsed;
                }
            } catch (IOException | ParseException e) {
                System.err.println("Could not read JSON file: " + e.getMessage());
            }
        }
        return messageArray;
    }
    
    private static boolean writeJsonArray(JSONArray messageArray, String filePath) {
        boolean success = false;
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(messageArray.toJSONString());
            writer.flush();
            success = true;
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
        return success;
    }

    private static String getField(JSONObject messageObject, String key) {
        Object value = messageObject.get(key);
        String result = "";
        if (value != null) {
            result = value.toString();
        }
        return result;
    }
    
}
