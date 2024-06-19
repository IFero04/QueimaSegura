package com.example.queimasegura.user.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.queimasegura.R
import com.example.queimasegura.common.reqPerm.RequestActivity


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_u, container, false)

        val addImageView = view.findViewById<ImageView>(R.id.add)
        addImageView.setOnClickListener {
            val intent = Intent(activity, RequestActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
