package com.example.queimasegura.common

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.queimasegura.R

class QueimaDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queima_details)

        val dateTextView: TextView = findViewById(R.id.date_text)
        val stateTextView: TextView = findViewById(R.id.state_text)
        val backButton: ImageView = findViewById(R.id.backButton)

        val type = intent.getStringExtra("type")
        val date = intent.getStringExtra("date")
        val state = intent.getStringExtra("state")

        dateTextView.text = date
        stateTextView.text = state

        backButton.setOnClickListener {
            finish()
        }
    }
}
