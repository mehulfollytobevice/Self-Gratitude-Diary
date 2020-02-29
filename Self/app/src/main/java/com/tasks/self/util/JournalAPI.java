package com.tasks.self.util;

import android.app.Application;

public class JournalAPI extends Application {
//    to make it visible throughout the application we need to mention it in the manifest file
    private String username;
    private String userId;
    private static JournalAPI instance;

    public JournalAPI() {
    }

    public static JournalAPI getInstance(){
        if (instance==null)
            instance=new JournalAPI();
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
