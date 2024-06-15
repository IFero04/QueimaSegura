package com.example.queimasegura.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.admin.model.User

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.username)
        val email: TextView = view.findViewById(R.id.email)
        val type: TextView = view.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_row_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.username.text = user.username
        holder.email.text = user.email
        holder.type.text = user.type

        val backgroundColor = if (position % 2 == 0) {
            holder.itemView.context.getColor(R.color.white)
        } else {
            holder.itemView.context.getColor(R.color.colorAccent)
        }
        holder.itemView.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount() = userList.size
}
