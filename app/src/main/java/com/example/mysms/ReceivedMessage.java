package com.example.mysms;

public class ReceivedMessage {
    private String content;
    private String timestamp;
    private int Contact_id;

    public ReceivedMessage(String content, String timestamp, int Contact_id) {
        this.content = content;
        this.timestamp = timestamp;
        this.Contact_id = Contact_id;
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

    public int getContact_id() {
        return Contact_id;
    }

    public void setContact_id(String sender) {
        this.Contact_id = Contact_id;
    }
}
