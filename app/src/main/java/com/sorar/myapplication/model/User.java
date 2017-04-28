package com.sorar.myapplication.model;

/**
 * Created by sorar on 1/3/2017.
 */

public class User {
    private String userName,userID;

    public User(String userName,String userID){
        this.setUserID(userID);
        this.setUserName(userName);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
