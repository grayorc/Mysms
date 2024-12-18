package com.example.mysms;

public class SentMessage {
    private int SentTo;
    private String content;
    private String timestamp;
    private String isSent;

    public SentMessage(int SentTo, String content, String timestamp, String isSent) {
        this.SentTo = SentTo;
        this.content = content;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public int getSentTo() {
        return SentTo;
    }

    public void setSentTo(int sentTo) {
        SentTo = sentTo;
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
