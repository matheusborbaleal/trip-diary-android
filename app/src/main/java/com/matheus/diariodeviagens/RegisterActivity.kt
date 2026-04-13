package com.matheus.diariodeviagens

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

        val editEmail = findViewById<EditText>(R.id.editEmailRegister)
        val editPassword = findViewById<EditText>(R.id.editPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val textBackToLogin = findViewById<TextView>(R.id.textBackToLogin)

        btnRegister.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
            }
        }

        textBackToLogin.setOnClickListener {
            finish()
        }
    }
}