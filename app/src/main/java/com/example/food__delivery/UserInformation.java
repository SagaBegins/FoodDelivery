package com.example.food__delivery;

public class UserInformation {
    public String num, name, id;

    public UserInformation() {
    }

    public UserInformation(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public UserInformation(String name, String num, String id) {
        this.id = id;
        this.num = num;
        this.name = name;
    }
}
