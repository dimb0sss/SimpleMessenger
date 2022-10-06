package com.lvovds.simplemessenger;

public class User {
    private String id;
    private String name;
    private String lastName;
    private  int age;
    private boolean online;
    private boolean typing;
    private String token;
    private String lastOnlineInfo;

    public User(String id, String name, String lastName, int age, boolean online, boolean typing, String token, String lastOnlineInfo) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.online = online;
        this.typing = typing;
        this.token = token;
        this.lastOnlineInfo = lastOnlineInfo;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }

    public String getToken() {
        return token;
    }

    public String getLastOnlineInfo() {
        return lastOnlineInfo;
    }

    public boolean isTyping() {
        return typing;
    }
}
