package com.example.queimasegura

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.common.IntroSliderActivity
import com.example.queimasegura.common.SplashActivity
import com.example.queimasegura.retrofit.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        initViewModels()

        viewModel.startApp()
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
}