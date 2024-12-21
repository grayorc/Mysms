package com.example.mysms;

public class SentMessage {
    private int ContactId;
    private String content;
    private String timestamp;
    private String isSent;

    public SentMessage(int ContactId, String content, String timestamp, String isSent) {
        this.ContactId = ContactId;
        this.content = content;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public int getContactId() {
        return ContactId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsSent() {
        return isSent;
    }

    public void setIsSent(String isSent) {
        this.isSent = isSent;
    }
}
