package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val editEmail = findViewById<EditText>(R.id.editEmailLogin)
        val editPassword = findViewById<EditText>(R.id.editPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val textGoToRegister = findViewById<TextView>(R.id.textGoToRegister)

        textGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

                            val dao = AppDatabase.getDatabase(this).tripDao()
                            dao.deleteAllTrips()

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao fazer login. Verifique seus dados.", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha e-mail e senha.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}