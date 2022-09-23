package com.lvovds.simplemessenger.notification;

import com.google.gson.annotations.SerializedName;

public class MessageSend {
    @SerializedName("to")
    private String to;
    @SerializedName("notification")
    private Notification notification;

    public MessageSend(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public Notification getNotification() {
        return notification;
    }
}
