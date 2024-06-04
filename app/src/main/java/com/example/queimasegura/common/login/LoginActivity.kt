package com.example.queimasegura.common.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.queimasegura.R
import com.example.queimasegura.common.RegisterActivity
import com.example.queimasegura.retrofit.model.Login

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
                val myLogin = Login(email, password)
                viewModel.loginUser(myLogin)
                viewModel.loginResponse.observe(this, Observer { response->
                    if(response.isSuccessful){
                        Log.d("RESPONSE", response.body()?.result?.userId!!)
                        Log.d("RESPONSE", response.body()?.result?.sessionId!!)
                    } else {
                        Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        val signUpPromptPart2: TextView = findViewById(R.id.textViewSignUpPromptPart2)

        signUpPromptPart2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
