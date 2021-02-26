package com.project.consultadoctor.Modal;

public class ModelChat {
    String message,receiver,sender,time, date,imageUrl;

    boolean isSeen;

    public ModelChat() {
    }

    public ModelChat(String message, String receiver, String sender, String time, String date, boolean isSeen,String imageUrl) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
        this.date = date;
        this.isSeen = isSeen;
        this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
