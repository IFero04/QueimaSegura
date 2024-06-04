package com.example.queimasegura.common.login


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.queimasegura.R
import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    private  lateinit var viewModel: LoginViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        val text: TextView = findViewById(R.id.textViewDescription)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
            }
        }

        val repository = Repository()
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        viewModel.getRoot()
        viewModel.myResponse.observe(this, Observer { response ->
            if(response.isSuccessful) {
                Log.d("RESPONSE", response.body()?.apiName!!)
            }
        })
    }


}