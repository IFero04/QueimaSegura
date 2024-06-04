package com.example.queimasegura.common.reqPerm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.queimasegura.R

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reqPermsMapPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnListeners()
    }

    private fun btnListeners() {
        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            startActivity(intent)
        }
    }
}