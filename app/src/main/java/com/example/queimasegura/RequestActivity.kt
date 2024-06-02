package com.example.queimasegura

import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class RequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_request)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permissionRequestPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val radioGroupLocation = findViewById<RadioGroup>(R.id.radioGroupLocation)
        setupRadioGroupListener(radioGroupLocation)
        setInitialFragment()
    }

    private fun setupRadioGroupListener(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            handleRadioButtonSelection(checkedId)
        }
    }

    private fun setInitialFragment() {
        replaceFragment(PostCodeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    private fun handleRadioButtonSelection(checkedId: Int) {
        val fragment = when (checkedId) {
            R.id.radioButtonPostCode -> PostCodeFragment()
            R.id.radioButtonMap -> MapFragment()
            else -> null
        }
        fragment?.let {
            replaceFragment(it)
        }
    }
}