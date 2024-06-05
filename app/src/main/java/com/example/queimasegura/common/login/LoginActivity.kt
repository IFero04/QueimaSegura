package com.example.queimasegura.common.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.RegisterActivity
import com.example.queimasegura.retrofit.model.ErrorApi
import com.example.queimasegura.retrofit.model.LoginSend
import com.example.queimasegura.retrofit.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initViewModels()

        val emailTextEdit = findViewById<EditText>(R.id.editTextEmail)
        val passwordTextEdit = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailTextEdit.text.toString()
            val password = passwordTextEdit.text.toString()

            if(inputCheck(email, password)){
                val myLoginSend = LoginSend(email, password)
                viewModel.loginUser(myLoginSend)
            }

        }

        val signUpPromptPart2: TextView = findViewById(R.id.textViewSignUpPromptPart2)

        signUpPromptPart2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]


        viewModel.loginResponse.observe(this) { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorApi>() {}.type
                val errorApiResponse: ErrorApi? = gson.fromJson(response.errorBody()?.charStream(), type)
                Toast.makeText(this, errorApiResponse?.detail, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputCheck(email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }
}
