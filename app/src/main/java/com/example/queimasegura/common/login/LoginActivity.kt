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
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.RegisterActivity
import com.example.queimasegura.retrofit.model.Login
import com.example.queimasegura.retrofit.repository.Repository

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            login()
        }

        val signUpPromptPart2: TextView = findViewById(R.id.textViewSignUpPromptPart2)

        signUpPromptPart2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun login() {
        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        val myLogin = Login("afonsofariatech@gmail.com", "e10adc3949ba59abbe56e057f20f883e")
        viewModel.loginUser(myLogin)
        viewModel.loginResponse.observe(this, Observer { response ->
            if(response.isSuccessful){
                Log.d("LOGIN", response.body()?.result?.userId.toString())
                Log.d("LOGIN", response.code().toString())
                Log.d("LOGIN", response.message().toString())
            } else{
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
