package com.example.mainactivity.activities

//Dialog libraries


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mainactivity.R
import com.example.mainactivity.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
/**
 * LoginActivity handles the user login process. It provides interfaces for user input
 * and directs users to either the main or faculty activity based on their credentials.
 * **/
class LoginActivity : AppCompatActivity() {


    // Late initialization of binding to link with the layout
    private  lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        // overloading onCreate method with savedInstanceState parameter KF 11/22/2023
        super.onCreate(savedInstanceState)

        // binding variable to java class passing layoutInflater containing the layout data KF 11/22/2023
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // setting the content view to the root of the binding variable KF 11/22/2023
        setContentView(binding.root)


        // setting the onClickListener for the login button KF 11/22/2023

        binding.loginButton.setOnClickListener {



            // creating an intent to start the MainActivity KF 11/22/2023
            // Changed to display new activity FacultyActivity BG 11/28/2023
            val intentFacultyActivity = Intent(this, FacultyActivity::class.java)

            val intentMainActivity = Intent(this, MainActivity::class.java)

            val usernameEditText = findViewById<TextInputEditText>(R.id.login_username_edittext)

            val username = usernameEditText.text.toString()

            val passcodeEditText = findViewById<TextInputEditText>(R.id.login_password_edittext)

            val passcode = Credential.digestPasscode(passcodeEditText.text.toString())

            val credential = Credential(username, passcode)

            val dbMan = DBManager()

            try
            {
                if(dbMan.tryLogin(credential))
                {
                    if(credential.isFaculty)
                        startActivity(intentFacultyActivity)
                    else
                        startActivity(intentMainActivity)
                }
                else
                    showLoginError()
            }
            catch(e: RuntimeException)
            {
                showPushError(e.toString())
            }
            // Now you can use 'username' as a String

            // starting the MainActivity KF 11/22/2023
        }

        // setting the onClickListener for the register button KF 11/22/2023
        binding.registerButton.setOnClickListener() {

            // creating an intent to start the RegistrationActivity KF 11/22/2023
            val intent = Intent(this, RegistrationActivity::class.java)

            // starting the RegistrationActivity KF 11/22/2023
            startActivity(intent)
        }

        // Removed old view binding code KF 11/22/2023
        //setContentView(R.layout.activity_login)


    }

    /**
     * Shows an error dialog if login fails.
     * **/
    private fun showLoginError() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Login Error")
        builder.setMessage("Your username and/or password is incorrect.")

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Shows a dialog with a custom error message
     * @param msg The message to be displayed in the dialog
     * **/
    private fun showPushError(msg: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Login Error")
        builder.setMessage(msg)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
