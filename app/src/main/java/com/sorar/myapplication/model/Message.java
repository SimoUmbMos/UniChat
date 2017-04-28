package com.sorar.myapplication.model;

public class Message{
    private String messageBody,sender, uidSender, date;

    public Message(String messageBody,String sender,String uidSender,String date) {
        this.setDate(date);
        this.setMessageBody(messageBody);
        this.setSender(sender);
        this.setUidSender(uidSender);
    }

    public String getUidSender() {
        return uidSender;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUidSender(String uidSender) {
        this.uidSender = uidSender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
