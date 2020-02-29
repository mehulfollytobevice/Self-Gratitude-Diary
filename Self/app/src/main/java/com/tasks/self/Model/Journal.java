package com.tasks.self.Model;

import com.google.firebase.Timestamp;

public class Journal {
    private String imageURL;
    private String userid;
    private String username;
    private com.google.firebase.Timestamp timeadded;
    private String title;
    private String thoughts;

    public Journal() {
    }

    public Journal(String imageURL, String userid, String username, com.google.firebase.Timestamp timeadded, String title, String thoughts) {
        this.imageURL = imageURL;
        this.userid = userid;
        this.username = username;
        this.timeadded = timeadded;
        this.title = title;
        this.thoughts = thoughts;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Timestamp getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        this.timeadded = timeadded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }
}
