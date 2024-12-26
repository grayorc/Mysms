package com.example.mysms;

public class ChatMessage {
    private String message;
    private String timestamp;
    private int isSent;

    private int Contact_id;

    public ChatMessage(String message, String timestamp, int isSent, int Contact_id) {
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
        this.Contact_id = Contact_id;

    }

    public int getContact_id() {
        return Contact_id;
    }

    public void setContact_id(int Contact_id) {
        this.Contact_id = Contact_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int isSent() {
        return isSent;
    }

    public void setSent(int sent) {
        isSent = sent;
    }
}
