package com.example.ktran.wannabetinder.models;

/**
 * Created by ktran on 11/26/17.
 */

public class User {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String name;
    private String password;
}
