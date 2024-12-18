package com.example.mysms;

public class ChatMessage {
    private String message;
    private String timestamp;
    private boolean isSent;

    public ChatMessage(String message, String timestamp, boolean isSent) {
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isSent() {
        return isSent;
    }
}
