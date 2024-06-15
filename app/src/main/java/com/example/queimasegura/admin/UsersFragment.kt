package com.example.queimasegura.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.user.User
import com.example.queimasegura.common.user.UserAdapter

class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: List<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUserTable)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        userList = List(10) { index ->
            User("Username$index", "email$index@example.com", "Manager")
        }

        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

        return view
    }
}