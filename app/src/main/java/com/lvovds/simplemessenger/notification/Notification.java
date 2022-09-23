package com.lvovds.simplemessenger.notification;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;

    public Notification(String body, String title) {
        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
}
