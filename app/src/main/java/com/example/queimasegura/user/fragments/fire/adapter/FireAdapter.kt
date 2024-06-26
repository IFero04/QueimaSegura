package com.example.queimasegura.user.fragments.fire.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.detail.queima.QueimaDetailsActivity
import com.example.queimasegura.common.detail.queimada.QueimadaDetailsActivity
import com.example.queimasegura.room.entities.Fire


class FireAdapter(
    private val context: Context,
    private var fires: List<Fire>
): RecyclerView.Adapter<FireAdapter.FireViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_card, parent, false)

        return FireViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FireViewHolder, position: Int) {
        val fire = fires[position]
        holder.typeTextView.text = fire.type
        holder.dateTextView.text = fire.date
        holder.stateTextView.text = fire.status

        holder.itemView.setOnClickListener {
            val intent = when (fire.type) {
                "Queima" -> Intent(context, QueimaDetailsActivity::class.java)
                "Burning" -> Intent(context, QueimaDetailsActivity::class.java)
                "Queimada" -> Intent(context, QueimadaDetailsActivity::class.java)
                "Controlled Burning" -> Intent(context, QueimadaDetailsActivity::class.java)
                else -> Intent(context, QueimaDetailsActivity::class.java)
            }
            intent.apply {
                putExtra("type", fire.type)
                putExtra("date", fire.date)
                putExtra("state", fire.status)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = fires.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateFires(newFires: List<Fire>) {
        fires = newFires
        notifyDataSetChanged()
    }

    inner class FireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.type)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val stateTextView: TextView = itemView.findViewById(R.id.state)
    }
}