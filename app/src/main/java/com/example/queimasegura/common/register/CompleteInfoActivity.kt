package com.example.queimasegura.common.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.retrofit.model.CreateUserSend
import com.example.queimasegura.retrofit.model.ErrorApi
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.user.UserActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CompleteInfoActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_info)

        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""

        initViewModels()

        val fullNameTextEdit = findViewById<EditText>(R.id.editTextName)
        val nifTextEdit = findViewById<EditText>(R.id.editTextNif)

        findViewById<Button>(R.id.buttonFinish).setOnClickListener {
            val fullName = fullNameTextEdit.text.toString()
            val nif = nifTextEdit.text.toString()

            try {
                inputCheck(fullName, nif)

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.createUser(CreateUserSend(fullName, email, password, nif))
                } else {
                    Toast.makeText(this, "Email or Password is missing", Toast.LENGTH_SHORT).show()
                }
            } catch (error: Exception) {
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java).apply {
                putExtra("EMAIL", email)
                putExtra("PASSWORD", password)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.clickHereButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButtonAdd).setOnClickListener {
            Toast.makeText(this, "Add logic", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = RegisterViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        viewModel.createUserResponse.observe(this) { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorApi>() {}.type
                val errorApiResponse: ErrorApi? = gson.fromJson(response.errorBody()?.charStream(), type)
                Toast.makeText(this, errorApiResponse?.detail, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputCheck(name: String, nif: String) {
        if (TextUtils.isEmpty(name))
            throw IllegalArgumentException("Name cannot be empty")
        if (TextUtils.isEmpty(nif))
            throw IllegalArgumentException("NIF cannot be empty")
        if (nif.length != 9 || !nif.all { it.isDigit() })
            throw IllegalArgumentException("NIF must be a 9-digit number")
    }
}
