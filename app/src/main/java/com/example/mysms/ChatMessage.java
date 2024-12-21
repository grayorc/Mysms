package com.example.mysms;

public class ChatMessage {
    private String message;
    private String timestamp;
    private boolean isSent;
    //TODO: add a bool to recognize if its a sender or receiver

    public ChatMessage(String message, String timestamp, boolean isSent) {
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

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
