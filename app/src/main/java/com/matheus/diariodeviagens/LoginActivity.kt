package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.text.InputType
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

    private val emailFragment = GenericInputFragment()
    private val passwordFragment = GenericInputFragment()
    private val buttonFragment = GenericButtonFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerEmail, emailFragment)
            .replace(R.id.containerPassword, passwordFragment)
            .replace(R.id.containerButton, buttonFragment)
            .commitNow()

        emailFragment.setup(
            label = "E-mail",
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        )

        passwordFragment.setup(
            label = "Senha",
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        )

        buttonFragment.setup(text = "Entrar") {
            performLogin()
        }

        findViewById<TextView>(R.id.textForgotPassword).setOnClickListener {
            val email = emailFragment.getText()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Digite seu e-mail no campo acima.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.textGoToRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {
        val email = emailFragment.getText()
        val password = passwordFragment.getText()

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
                        Toast.makeText(this, "Falha na autenticação: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
        }
    }
}