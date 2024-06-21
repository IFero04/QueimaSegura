package com.example.queimasegura.user.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.reqPerm.RequestActivity
import com.example.queimasegura.retrofit.repository.Repository


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_u, container, false)

        initViewModels()

        val addImageView = view.findViewById<ImageView>(R.id.add)
        addImageView.setOnClickListener {
            val intent = Intent(activity, RequestActivity::class.java)
            startActivity(intent)
        }

        val usernameTextView = view.findViewById<TextView>(R.id.username_welcome)
        viewModel.username.observe(viewLifecycleOwner) { username ->
            username?.let {
                usernameTextView.text = it
            }
        }

        viewModel.fetchUsername()

        return view
    }

    private fun initViewModels(){
        val repository = Repository()
        val viewModelFactory = HomeViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }
}
