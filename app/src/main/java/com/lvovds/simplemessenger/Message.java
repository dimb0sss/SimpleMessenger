package com.lvovds.simplemessenger;

public class Message {

    private String text;
    private String sendId;
    private String receiverId;

    public Message(String text, String sendId, String receiverId) {
        this.text = text;
        this.sendId = sendId;
        this.receiverId = receiverId;
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
}
