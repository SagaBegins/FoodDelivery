package com.example.food__delivery.HelperModal;

public class UserModal {
    public String num, name, id;
    public boolean admin;

    public UserModal() {
    }

    public UserModal(String num, String name) {
        this.num = num;
        this.name = name;
        this.admin = false;
    }

    public UserModal(String name, String num, String id) {
        this.id = id;
        this.num = num;
        this.name = name;
    }
}
