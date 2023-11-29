package com.example.mainactivity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainactivity.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Added binding variable KF 11/22/2023
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
            val intent = Intent(this, MainActivity::class.java)

            // starting the MainActivity KF 11/22/2023
            startActivity(intent)
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
}
