package com.lvovds.simplemessenger;

public class Message {

    private String text;
    private String sendId;
    private String receiverId;
    private String sendingTime;
    private String messageId;

    public Message(String text, String sendId, String receiverId, String sendingTime, String messageId) {
        this.text = text;
        this.sendId = sendId;
        this.receiverId = receiverId;
        this.sendingTime = sendingTime;
        this.messageId = messageId;
    }

    public Message(String text, String sendId, String receiverId, String sendingTime) {
        this.text = text;
        this.sendId = sendId;
        this.receiverId = receiverId;
        this.sendingTime = sendingTime;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public String getSendId() {
        return sendId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public String getMessageId() {
        return messageId;
    }
}
