package com.example.queimasegura


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.queimasegura.retrofit.api.ApiService
import com.example.queimasegura.retrofit.api.RetrofitInstance
import com.example.queimasegura.retrofit.model.Root
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

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

        val request = RetrofitInstance.buildService(ApiService::class.java)
        val call = request.getApiName()

        call.enqueue(object: Callback<Root>{
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful){
                   text.text = response.body()?.Hello.toString()
                }
            }
            override fun onFailure(call: Call<Root>, t: Throwable) {
                text.text = t.message
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}