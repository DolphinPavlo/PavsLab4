package com.example.lab3;

import java.util.ArrayList;

public class Message {
 /*   public String message;
    public boolean isSend;

    public Message(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }
}*/

    long id;
    String text;
    Boolean isSent;
    Boolean isReceived;

    public Message() {
        this.text = "";
        this.isSent = false;
        this.isReceived = false;

    }
    public Message (long i, String t, Boolean s, Boolean r){
        this.id = i;
        this.text = t;
        this.isSent = s;
        this.isReceived = r;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public Boolean getReceived() {
        return isReceived;
    }

    public void setReceived(Boolean received) {
        isReceived = received;
    }





}