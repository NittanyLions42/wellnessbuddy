package com.example.mainactivity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.example.mainactivity.R
import com.example.mainactivity.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    // Added binding variable KF 12/1/2023
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Added binding variable for persistent data KF 12/1/2023
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Removed old view binding code KF 12/1/2023
        // setContentView(R.layout.activity_registration)

        // Set the switch state change listener
        val switchCompat: SwitchCompat = findViewById(R.id.student_faculty_switch)
        val facultyLabel: TextView = findViewById(R.id.faculty_label)
        val studentLabel: TextView = findViewById(R.id.student_label)

        // Initialize the switch state and labels when the activity is created
        switchCompat.isChecked = true
        updateLabelColors(facultyLabel, studentLabel, switchCompat.isChecked)

        // Handle the switch state changes
        switchCompat.setOnCheckedChangeListener { _, isChecked ->
            updateLabelColors(facultyLabel, studentLabel, isChecked)
        }
    }

    // Update the label colors based on the switch state
    private fun updateLabelColors(facultyLabel: TextView, studentLabel: TextView, isChecked: Boolean) {
        if (isChecked) {
            facultyLabel.setTextColor(ContextCompat.getColor(this, R.color.White_Out)) // Inactive color
            studentLabel.setTextColor(ContextCompat.getColor(this, R.color.PA_Creek)) // Active color
        } else {
            facultyLabel.setTextColor(ContextCompat.getColor(this, R.color.PA_Creek)) // Active color
            studentLabel.setTextColor(ContextCompat.getColor(this, R.color.White_Out)) // Inactive color
        }
    }
}
