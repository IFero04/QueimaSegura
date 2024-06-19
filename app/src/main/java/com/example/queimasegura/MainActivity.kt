package com.example.queimasegura

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.common.intro.IntroSliderActivity
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.user.UserActivity


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        initViewModels()

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        findViewById<View>(R.id.splash).setOnClickListener {
            if (isFirstRun()) {
                startActivity(Intent(this, IntroSliderActivity::class.java))
                finish()
            } else {
                viewModel.startApp()
            }
        }
    }

    private fun isFirstRun(): Boolean {
        val sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
        return isFirstRun
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        viewModel.appState.observe(this) { state ->
            when (state) {
                MainViewModel.AppState.HOME -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }
                MainViewModel.AppState.LOGIN -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                MainViewModel.AppState.ERROR -> {

                }
                null -> {
                    Toast.makeText(this, "SERVER ERROR", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
