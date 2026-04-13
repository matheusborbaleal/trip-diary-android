package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val editEmailLogin = findViewById<EditText>(R.id.editEmailLogin)
        val editPasswordLogin = findViewById<EditText>(R.id.editPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val textForgotPassword = findViewById<TextView>(R.id.textForgotPassword)
        val textGoToRegister = findViewById<TextView>(R.id.textGoToRegister)

        btnLogin.setOnClickListener {
            val email = editEmailLogin.text.toString()
            val password = editPasswordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val dao = AppDatabase.getDatabase(this@LoginActivity).tripDao()
                                dao.deleteAllTrips()

                                withContext(Dispatchers.Main) {
                                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        textForgotPassword.setOnClickListener {
            val email = editEmailLogin.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Digite seu e-mail acima.", Toast.LENGTH_SHORT).show()
            }
        }

        textGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}