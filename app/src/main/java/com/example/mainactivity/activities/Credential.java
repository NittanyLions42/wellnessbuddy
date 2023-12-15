package com.example.mainactivity.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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

    /**
     * Digests the given String into a byte array using SHA-256.
     *
     * @param input the String to be digested.
     * @return The byte array cast to a String.
     */
    public static String digestPasscode(String input)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            throw new RuntimeException("Error: " + nsae.getMessage());
        }
        byte[] outputByteArray = md.digest(input.getBytes());
        return Arrays.toString(outputByteArray);
    }

}