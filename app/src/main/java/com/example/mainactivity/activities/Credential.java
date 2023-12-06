package com.example.mainactivity.activities;

public class Credential {
    private final String userID;
    private final String passcode;
    private boolean isFaculty;

    public Credential(String userID, String passcode) {
        this.userID = userID;
        this.passcode = passcode;
    }


    public String getUserID() {
        return userID;
    }

    public void setFaculty(boolean value) {
        isFaculty = value;
    }

    public String getPasscode() {
        return passcode;
    }

    public boolean isFaculty() {
        return isFaculty;
    }

}