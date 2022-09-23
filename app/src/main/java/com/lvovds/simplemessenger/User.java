package com.lvovds.simplemessenger;

public class User {
    private String id;
    private String name;
    private String lastName;
    private  int age;
    private boolean online;
    private String token;

    public User(String id, String name, String lastName, int age, boolean online, String token) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.online = online;
        this.token = token;
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
}
