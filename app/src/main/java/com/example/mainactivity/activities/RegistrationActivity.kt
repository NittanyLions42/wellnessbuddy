package com.example.mainactivity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.example.mainactivity.R

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

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
