package com.example.queimasegura.common

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.queimasegura.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            val completeProfileLayout = layoutInflater.inflate(R.layout.user_activity, findViewById(android.R.id.content), false)
            setContentView(completeProfileLayout)
        }
    }
}
