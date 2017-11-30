package com.example.ktran.wannabetinder.models;

/**
 * Created by ktran on 11/26/17.
 */

public class ServerResponse {
    private String message;
    private boolean success;
    private String token;
    private User user;
    private User[] users;
    private Friend friend;
    private Friend[] friends;

    public Friend getFriend() {
        return friend;
    }

    public Friend[] getFriends() {
        return friends;
    }

    public User[] getUsers() {return users;}
    public User getuser(){return user;}
    public String getMessage() {return message;}
    public String getjsonToken(){return token;}
    public boolean getSuccess() { return success;}
}
