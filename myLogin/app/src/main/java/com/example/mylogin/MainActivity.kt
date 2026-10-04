package com.example.mylogin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Connect views using the ids we created
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val cbFootball = findViewById<Chip>(R.id.cbFootball)
        val cbCricket = findViewById<Chip>(R.id.cbCricket)
        val cbBadminton = findViewById<Chip>(R.id.cbBadminton)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // 1. Email
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // 2. Password
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // 3. Confirm password
            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Please confirm your password"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // 4. Gender (-1 means nothing selected)
            if (rgGender.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 5. Hobbies
            if (!cbFootball.isChecked && !cbCricket.isChecked && !cbBadminton.isChecked) {
                Toast.makeText(this, "Please select at least one hobby", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 6. Terms and Conditions
            if (!cbTerms.isChecked) {
                Toast.makeText(this, "You must accept the Terms and Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // All checks passed, go to the second page
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
            finish() // optional: stops the user from coming back to the form with the Back button
        }
    }
}