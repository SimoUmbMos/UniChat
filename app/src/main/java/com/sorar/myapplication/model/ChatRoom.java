package com.sorar.myapplication.model;

public class ChatRoom {
    private String Title,Creator,CreatorID,Password;
    private boolean isPrivate;


    public ChatRoom(String Title,String Password,String Creator,String CreatorID){
        this.setPrivate(true);
        this.setTitle(Title);
        this.setPassword(Password);
        this.setCreator(Creator);
        this.setCreatorID(CreatorID);
    }

    public ChatRoom(String Title,String Creator,String CreatorID){
        this.setPrivate(false);
        this.setTitle(Title);
        this.setPassword("");
        this.setCreator(Creator);
        this.setCreatorID(CreatorID);
    }

    public boolean AuthAccess(String password) {
        return ((!isPrivate) || (this.getPassword().equals(password)));
    }

    public String getTitle() {
        return Title;
    }

    public String getPassword() {
        return Password;
    }

    public String getCreator() {
        return Creator;
    }

    public String getCreatorID() {
        return CreatorID;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public String isPrivateToString() {
        if(this.isPrivate()){
            return "Private";
        }else{
            return "Public";
        }
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public void setCreatorID(String creatorID) {
        CreatorID = creatorID;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPrivate(boolean isprivate) {
        this.isPrivate = isprivate;
    }
}
