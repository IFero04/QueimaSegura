package com.example.queimasegura.common

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.QueimaDetailsActivity
import com.example.queimasegura.common.QueimadaDetailsActivity

class PedidoAdapter(
    private val context: Context,
    private var pedidos: List<Pedido>
) : ListAdapter<Pedido, PedidoAdapter.PedidoViewHolder>(PedidoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return PedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido)
    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    fun updatePedidos(newPedidos: List<Pedido>) {
        val diffResult = DiffUtil.calculateDiff(PedidoDiffCallback(pedidos, newPedidos))
        pedidos = newPedidos
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeTextView: TextView = itemView.findViewById(R.id.type)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val stateTextView: TextView = itemView.findViewById(R.id.state)

        fun bind(pedido: Pedido) {
            typeTextView.text = pedido.type
            dateTextView.text = pedido.date
            stateTextView.text = pedido.state

            itemView.setOnClickListener {
                val intent = when (pedido.type) {
                    "Queima" -> Intent(context, QueimaDetailsActivity::class.java)
                    "Queimada" -> Intent(context, QueimadaDetailsActivity::class.java)
                    else -> return@setOnClickListener
                }.apply {
                    putExtra("type", pedido.type)
                    putExtra("date", pedido.date)
                    putExtra("state", pedido.state)
                }
                context.startActivity(intent)
            }
        }
    }

    class PedidoDiffCallback(
        private val oldList: List<Pedido>,
        private val newList: List<Pedido>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
