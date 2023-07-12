package com.codingstuff.BookApp.utils.model;

import java.io.Serializable;


public class Account implements Serializable {

    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;

    public Account() {
    }

    public Account(String userName, String password, String email, String address, String phone) {
        this.username = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
