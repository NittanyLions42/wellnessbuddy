package com.example.mainactivity.activities;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbManager {
    public boolean localVar;

    dbManager()
    {
        localVar  =true;
    }
    public boolean returnTrue()
    {
        return localVar;
    }

    public boolean tryLogin()
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

                return true;
            }
        }
        catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }
        return false;
    }

    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            ConnectionURL = "jdbc:jtds:sqlserver://wellnessbuddy.database.windows.net:1433;DatabaseName=wellnessbuddyDB;user=java@wellnessbuddy;password=WellnessBuddy23;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException e) {
            Log.e("Error: ", e.getMessage());
            //throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.e("Error: ", e.getMessage());
            //throw new RuntimeException(e);
        } catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }

        return connection;
    }
}
