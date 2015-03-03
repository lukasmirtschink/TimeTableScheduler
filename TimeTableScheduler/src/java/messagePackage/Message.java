/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messagePackage;

import java.util.Iterator;
import java.util.List;
import toolsPackage.Database;

/**
 *
 * @author jjor1
 */
public class Message {
    private String subject;
    private String body;
    private boolean status;
    private String sender;
    private MessageType type;

    public Message(String subject, String body, boolean status, String sender, MessageType type) {
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.sender = sender;
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
    
    public boolean sendMessage(List<String> userIds) {
        boolean result = false;
        Database db = Database.getSetupDatabase();
        
        result = db.insert("INSERT INTO Messages (subject, messageType, body, from_id, status)" +
                           "VALUES ('" + subject + "', '" + type.getName() + "', '" + body + "', '" + sender + "', 0);");
        
        if (result) {
            int messageId = db.getPreviousAutoIncrementID("Messages");
            String insertValues = getMessageInsertValues(messageId, userIds);
            for (String id : userIds) {
                result = db.insert("INSERT INTO MessageFor (uid, messageid) " + insertValues);
            }
        }
        
        return result;
    }
    
    private String getMessageInsertValues(int meetingID, List<String> userIds) {
        String values = "";
        Iterator<String> ids = userIds.iterator();
        while (ids.hasNext()) {
            values += "(" + ids.next() + ", " + meetingID + ")" + (ids.hasNext() ? ", " : ";");
        }
        return values;
    }
}
