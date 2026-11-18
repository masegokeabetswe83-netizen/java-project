/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

/**
 *
 * @author maseg
 */
import java.util.Arrays;
public class ManageMessages {
    
    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    private static final int MAX_MESSAGES = 100;
    
    // Parallel arrays (same index = same message)
    private String[] messageIDs = new String[MAX_MESSAGES];
    private String[] messageHashes = new String[MAX_MESSAGES];
    private String[] recipients = new String[MAX_MESSAGES];
    private String[] messageTexts = new String[MAX_MESSAGES];
    private String[] flags = new String[MAX_MESSAGES];
    private int messageCount = 0;
    
    // Arrays required by the brief
    private String[] sentMessages = new String[MAX_MESSAGES];
    private int sentCount = 0;
    private String[] disregardedMessages = new String[MAX_MESSAGES];
    private int disregardedCount = 0;
    private String[] storedMessages = new String[MAX_MESSAGES];
    private int storedCount = 0;
    
    public boolean addMessage(String messageID, String messageHash, String recipient,
            String messageText, String flag) {
        boolean added = false;

        if (messageCount < MAX_MESSAGES) {
            messageIDs[messageCount] = messageID;
            messageHashes[messageCount] = messageHash;
            recipients[messageCount] = recipient;
            messageTexts[messageCount] = messageText;
            flags[messageCount] = flag;
            messageCount++;

            if (flag.equals(FLAG_SENT)) {
                sentMessages[sentCount] = messageText;
                sentCount++;
            } else if (flag.equals(FLAG_STORED)) {
                storedMessages[storedCount] = messageText;
                storedCount++;
            } else if (flag.equals(FLAG_DISREGARD)) {
                disregardedMessages[disregardedCount] = messageText;
                disregardedCount++;
            }
            added = true;
        }
        return added;
    }
    
    public String[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentCount);
    }

    public String[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedCount);
    }

    public String[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedCount);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageCount);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageCount);
    }

    public int getMessageCount() {
        return messageCount;
    }
    
    public int loadStoredFromJson(String filePath) {
        String[][] rows = StorageJSON.readStoredMessages(filePath);
        int loaded = 0;

        int index = 0;
        while (index < rows.length) {
            if (findIndexByID(rows[index][0]) == -1) {
                boolean added = addMessage(rows[index][0], rows[index][1],
                        rows[index][2], rows[index][3], FLAG_STORED);
                if (added) {
                    loaded++;
                }
            }
            index++;
        }
        return loaded;
    }
    
    /**
     * 2c. Search by message ID - returns recipient and message for display.
     */
    public String displayMessageDetailsByID(String messageID) {
        int position = findIndexByID(messageID);
        String result = "Message ID not found.";

        if (position != -1) {
            result = "Recipient: " + recipients[position]
                    + "\nMessage: " + messageTexts[position];
        }
        return result;
    }

    public boolean saveStoredToJson(String filePath) {
        String[][] rows = new String[storedCount][4];

        int rowIndex = 0;
        int index = 0;
        while (index < messageCount) {
            if (flags[index].equals(FLAG_STORED)) {
                rows[rowIndex][0] = messageIDs[index];
                rows[rowIndex][1] = messageHashes[index];
                rows[rowIndex][2] = recipients[index];
                rows[rowIndex][3] = messageTexts[index];
                rowIndex++;
            }
            index++;
        }
        return StorageJSON.overwriteMessages(rows, filePath);
    }
    
    public String displaySenderAndRecipient(String senderName) {
        StringBuilder result = new StringBuilder();

        int index = 0;
        while (index < messageCount) {
            if (flags[index].equals(FLAG_STORED)) {
                result.append("Sender: ").append(senderName)
                      .append(" | Recipient: ").append(recipients[index]).append("\n");
            }
            index++;
        }

        if (result.length() == 0) {
            result.append("No stored messages.");
        }
        return result.toString();
    }
    
    /**
     * 2b. Longest stored message.
     */
    public String findLongestStoredMessage() {
        String longest = "";

        int index = 0;
        while (index < storedCount) {
            if (storedMessages[index].length() > longest.length()) {
                longest = storedMessages[index];
            }
            index++;
        }
        return longest;
    }
    
    /**
     * 2c. Search by message ID - returns just the message text (used by the unit test).
     */
    public String searchByMessageID(String messageID) {
        int position = findIndexByID(messageID);
        String result = "Message ID not found.";

        if (position != -1) {
            result = messageTexts[position];
        }
        return result;
    }
    
    /**
     * 2d. All Sent or Stored messages for a particular recipient.
     */
    public String[] searchByRecipient(String recipient) {
        int matches = 0;
        int index = 0;
        while (index < messageCount) {
            if (recipients[index].equals(recipient) && !flags[index].equals(FLAG_DISREGARD)) {
                matches++;
            }
            index++;
        }

        String[] found = new String[matches];
        int foundIndex = 0;
        index = 0;
        while (index < messageCount) {
            if (recipients[index].equals(recipient) && !flags[index].equals(FLAG_DISREGARD)) {
                found[foundIndex] = messageTexts[index];
                foundIndex++;
            }
            index++;
        }
        return found;
    }
    
    /**
     * 2e. Delete a message using its hash.
     */
    public String deleteByHash(String messageHash) {
        int position = findIndexByHash(messageHash);
        String result = "Message hash not found.";

        if (position != -1) {
            String deletedText = messageTexts[position];
            String deletedFlag = flags[position];

            // Remove from the matching Sent/Stored/Disregarded array
            if (deletedFlag.equals(FLAG_SENT)) {
                sentCount = removeFromList(sentMessages, sentCount, deletedText);
            } else if (deletedFlag.equals(FLAG_STORED)) {
                storedCount = removeFromList(storedMessages, storedCount, deletedText);
            } else if (deletedFlag.equals(FLAG_DISREGARD)) {
                disregardedCount = removeFromList(disregardedMessages, disregardedCount, deletedText);
            }

            // Shift every parallel array left over the deleted position
            int shift = position;
            while (shift < messageCount - 1) {
                messageIDs[shift] = messageIDs[shift + 1];
                messageHashes[shift] = messageHashes[shift + 1];
                recipients[shift] = recipients[shift + 1];
                messageTexts[shift] = messageTexts[shift + 1];
                flags[shift] = flags[shift + 1];
                shift++;
            }
            messageCount--;
            messageIDs[messageCount] = null;
            messageHashes[messageCount] = null;
            recipients[messageCount] = null;
            messageTexts[messageCount] = null;
            flags[messageCount] = null;

            result = "Message: \"" + deletedText + "\" successfully deleted.";
        }
        return result;
    }
    
    /**
     * 2f. Report with the full details of all sent and stored messages.
     */
    public String displayReport() {
        StringBuilder report = new StringBuilder();
        int reported = 0;

        report.append("===== MESSAGE REPORT =====\n");
        int index = 0;
        while (index < messageCount) {
            if (!flags[index].equals(FLAG_DISREGARD)) {
                report.append("Message ID:   ").append(messageIDs[index]).append("\n");
                report.append("Message Hash: ").append(messageHashes[index]).append("\n");
                report.append("Recipient:    ").append(recipients[index]).append("\n");
                report.append("Message:      ").append(messageTexts[index]).append("\n");
                report.append("Status:       ").append(flags[index]).append("\n");
                report.append("--------------------------\n");
                reported++;
            }
            index++;
        }

        if (reported == 0) {
            report.append("No messages to report.\n");
        }
        return report.toString();
    }
    
    // ---------- private helpers ----------

    private int findIndexByID(String messageID) {
        int position = -1;
        int index = 0;
        while (index < messageCount && position == -1) {
            if (messageIDs[index].equals(messageID)) {
                position = index;
            }
            index++;
        }
        return position;
    }
    
    private int findIndexByHash(String messageHash) {
        int position = -1;
        int index = 0;
        while (index < messageCount && position == -1) {
            if (messageHashes[index].equals(messageHash)) {
                position = index;
            }
            index++;
        }
        return position;
    }
    
    // Removes the first match from a list array and returns the new count
    private int removeFromList(String[] list, int count, String value) {
        int position = -1;
        int index = 0;
        while (index < count && position == -1) {
            if (list[index].equals(value)) {
                position = index;
            }
            index++;
        }

        int newCount = count;
        if (position != -1) {
            int shift = position;
            while (shift < count - 1) {
                list[shift] = list[shift + 1];
                shift++;
            }
            list[count - 1] = null;
            newCount = count - 1;
        }
        return newCount;
    }
    
    
}
