package com.example.mainactivity.activities;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Objects;

public class dbManager {
    public boolean localVar;

    dbManager()
    {
        localVar  =true;
    }

    public boolean tryLogin(Credential credential) throws RuntimeException {
        try (Connection con = connectionclass()) {
            if(con == null)
                throw new RuntimeException("Can't connect to the database.");
            // Create a prepared statement with a parameterized query
            String sql  = "";//""SELECT passcode, isFaculty FROM credentials WHERE userID = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, credential.getUserID());

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if a matching record was found
                    if (resultSet.next()) {
                        // Retrieve passcode and isFaculty from the result set
                        String passcode = resultSet.getString("passcode");
                        boolean isFaculty = resultSet.getBoolean("isFaculty");

                        // Create a Credentials object with the retrieved values
                        //returnCredential = new Credentials(passcode, isFaculty);
                        credential.setFaculty(isFaculty);
                        return passcode.equals(credential.getPasscode());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception based on your application's needs
        }
        return false;
    }


    public boolean registerUser(String userID, String passcode, boolean isFaculty)
    {
        try {
            Connection con = connectionclass();
            if(con == null)
            {
                System.out.println("boo");
                return false;
            }
            else {
                System.out.println("it works!");

                String sql = "INSERT INTO credentials (userID, passcode, isFaculty) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    preparedStatement.setString(1, userID);
                    preparedStatement.setString(2, passcode);
                    preparedStatement.setBoolean(3, isFaculty);

                    // Execute the update
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User created successfully!");
                        return true;
                    } else {
                        System.out.println("Failed to create user.");
                        return false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    @SuppressLint({"NewApi", "AuthLeak"})
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            ConnectionURL = "jdbc:jtds:sqlserver://wellnessbuddy.database.windows.net:1433;DatabaseName=wellnessbuddyDB;user=java@wellnessbuddy;password=WellnessBuddy23;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException | ClassNotFoundException e) {
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
            //throw new RuntimeException(e);
        } catch (Exception e)
        {
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
        }

        return connection;
    }

    public boolean checkUserID(@NotNull String desiredUsername) {
        boolean usernameExists = false;

        // Using try-with-resources to automatically close resources
        try (Connection con = connectionclass()) {
            // The SQL query to check if the username exists
            String sqlQuery = "SELECT COUNT(*) AS userCount FROM credentials WHERE userID = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(sqlQuery)) {
                // Set the parameter in the prepared statement
                preparedStatement.setString(1, desiredUsername);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Retrieve the count from the result set
                        int userCount = resultSet.getInt("userCount");

                        // If the count is greater than 0, the username exists
                        usernameExists = userCount > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return usernameExists;
    }
}
