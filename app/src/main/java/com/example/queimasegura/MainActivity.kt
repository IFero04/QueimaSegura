package com.example.queimasegura

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.common.intro.IntroSliderActivity
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.user.HomeFragment
import com.example.queimasegura.user.UserActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        findViewById<View>(R.id.splash).setOnClickListener {
            if(isFirstRun()) {
                startActivity(Intent(this, IntroSliderActivity::class.java))
                finish()
            } else {
                initViewModels()
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

        viewModel.userState.observe(this) { state ->
            when (state) {
                MainViewModel.UserState.INTERNET_AUTH -> {
                    Log.d("UserState", "Have internet and auth")
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }
                MainViewModel.UserState.INTERNET_NO_AUTH -> {
                    Log.d("UserState", "Have internet but not auth")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                MainViewModel.UserState.NO_INTERNET_AUTH -> {
                    Log.d("UserState", "No internet but auth")
                }
                MainViewModel.UserState.NO_INTERNET_NO_AUTH -> {
                    Log.d("UserState", "No internet and not auth")
                }
                MainViewModel.UserState.UNKNOWN -> {
                    Log.d("UserState", "Unknown state")
                }
                null -> {
                    Log.d("UserState", "State is null")
                }
            }
        }
    }
}
