package com.example.queimasegura.common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.queimasegura.R
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.common.reqPerm.RequestActivity

class CompleteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_info)

        btnListeners()
    }

    private fun btnListeners() {
        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.clickHereButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonFinish).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButtonAdd).setOnClickListener {
            showToast("Add logic")
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}