package com.example.queimasegura.common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.queimasegura.R
import com.example.queimasegura.common.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        btnListeners()
    }

    private fun btnListeners() {
        findViewById<Button>(R.id.clickHereButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonContinue).setOnClickListener {
            val intent = Intent(this, CompleteInfoActivity::class.java)
            startActivity(intent)
        }
    }
}
