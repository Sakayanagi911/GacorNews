package com.example.gacornews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // Mengambil elemen UI dengan ID yang sesuai
        val emailEditText: EditText = findViewById(R.id.registerEmailEditText)
        val passwordEditText: EditText = findViewById(R.id.registerPasswordEditText)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginTextView: TextView = findViewById(R.id.loginTextView)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Memeriksa kesesuaian password
            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Melakukan registrasi pengguna
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Menavigasi ke halaman Login
        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
