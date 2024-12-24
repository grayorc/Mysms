package com.example.mysms;

public class ChatMessage {
    private String message;
    private String timestamp;
    private int isSent;

    public ChatMessage(String message, String timestamp, int isSent) {
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
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
