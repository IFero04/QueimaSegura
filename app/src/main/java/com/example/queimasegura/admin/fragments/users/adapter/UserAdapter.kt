package com.example.queimasegura.admin.fragments.users.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.admin.fragments.users.model.User


class UserAdapter(
    private val context: Context,
    private val userList: MutableList<User>,
    private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.username)
        val email: TextView = view.findViewById(R.id.email)
        var type: TextView = view.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_row_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.username.text = user.fullName
        holder.email.text = user.email
        holder.type.text = when (user.type) {
            2 -> context.getString(R.string.admin)
            1 -> context.getString(R.string.manager)
            else -> context.getString(R.string.user)
        }

        Log.d("USER", user.toString())
        if(user.deleted){
            holder.username.setTextColor(context.getColor(android.R.color.holo_red_dark))
        } else if (!user.active) {
            holder.username.setTextColor(context.getColor(android.R.color.holo_orange_dark))
        } else {
            holder.username.setTextColor(context.getColor(android.R.color.black))
        }

        val backgroundColor = if (position % 2 == 0) {
            holder.itemView.context.getColor(R.color.white)
        } else {
            holder.itemView.context.getColor(R.color.colorAccent)
        }
        holder.itemView.setBackgroundColor(backgroundColor)

        holder.itemView.setOnClickListener {
            itemClickListener(position)
        }
    }

    override fun getItemCount() = userList.size

    fun updateUserPermission(position: Int, newPermission: Int): Int {
        userList[position].type = newPermission
        notifyItemChanged(position)
        return userList[position].type
    }

    fun banUser(position: Int): Boolean {
        userList[position].active = !userList[position].active
        notifyItemChanged(position)
        return userList[position].active
    }

    fun deleteUser(position: Int): Boolean {
        userList[position].deleted = !userList[position].deleted
        notifyItemChanged(position)
        return userList[position].deleted
    }
}
