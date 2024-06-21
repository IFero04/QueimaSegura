package com.example.queimasegura

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
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

        initEvents()
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }


    private fun initEvents() {
        viewModel.appState.observe(this) { state ->
            when (state) {
                MainViewModel.AppState.INTRO -> navigateTo(IntroSliderActivity::class.java)
                MainViewModel.AppState.HOME -> navigateTo(UserActivity::class.java)
                MainViewModel.AppState.LOGIN -> navigateTo(LoginActivity::class.java)
                MainViewModel.AppState.ERROR -> handleAppError()
                null -> showErrorMessage("SERVER ERROR")
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { showErrorMessage(it) }
        }


        findViewById<View>(R.id.splash).setOnClickListener {
            if (isFirstRun()) {
                viewModel.firstRun()
            } else {
                viewModel.startApp()
            }
        }
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        finish()
    }

    private fun isFirstRun(): Boolean {
        val sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
        return isFirstRun
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun handleAppError() {
        // Handle any specific error scenario if needed
    }
}
