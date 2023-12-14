package com.example.mainactivity.activities;
/**
 * The Credential class represents user credentials within the system.
 * It stores a user ID, password, and flag indicating whether user is faculty.
 * **/
public class Credential {
    private final String userID;
    private final String passcode;
    private boolean isFaculty;

    /**
     * Constructor for Credential Class which Initializes the user ID and password
     *
     * @param userID The unique ID for the user
     * @param passcode The unique password for the user
     * **/
    public Credential(String userID, String passcode) {
        this.userID = userID;
        this.passcode = passcode;
    }

    /**
     * Gets the user ID
     *
     * @return A string representing user ID
     * **/
    public String getUserID() {
        return userID;
    }
    /**
     * Sets the faculty status of the user
     *
     * @param value Boolean indicating whether user is faculty
     * **/
    public void setFaculty(boolean value) {
        isFaculty = value;
    }

    /**
     * Gets the user password
     *
     * @return A string representing user password
     * **/
    public String getPasscode() {
        return passcode;
    }
    /**
     * Checks if use is a faculty member
     *
     * @return True is user is faculty
     * **/
    public boolean isFaculty() {
        return isFaculty;
    }

}