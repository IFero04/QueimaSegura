package com.example.queimasegura.user.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.Pedido
import com.example.queimasegura.common.PedidoAdapter

class RequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pedidos: List<Pedido>
    private lateinit var pedidoAdapter: PedidoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests_u, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val imageButtonFilter = view.findViewById<ImageButton>(R.id.imageButtonDate)
        imageButtonFilter.setOnClickListener { showFilterOptions(it) }

        pedidos = listOf(
            Pedido("Queimada", "2024-06-11", "Pending"),
            Pedido("Queima", "2024-06-11", "Completed"),
            Pedido("Queima", "2024-06-11", "In Progress"),
            Pedido("Queima", "2024-06-11", "Pending"),
            Pedido("Queima", "2024-06-11", "Completed"),
            Pedido("Queima", "2024-06-11", "In Progress"),
            Pedido("Queima", "2024-06-11", "Pending"),
            Pedido("Queima", "2024-06-11", "Completed"),
            Pedido("Queima", "2024-06-11", "In Progress"),
            Pedido("Queima", "2024-06-11", "Pending"),
            Pedido("Queima", "2024-06-11", "Completed"),
            Pedido("Queima", "2024-06-11", "In Progress"),


        ).sortedByDescending { it.date }

        pedidoAdapter = PedidoAdapter(requireContext(), pedidos)
        recyclerView.adapter = pedidoAdapter

        return view
    }

    private fun showFilterOptions(view: View) {
        try {
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                // Handle menu item clicks
                when (item.itemId) {
                    R.id.pending -> {
                        Log.d("RequestsFragment", "Filtering by state: Pending")
                        filterPedidosByState("Pending")
                    }
                    R.id.approved -> {
                        Log.d("RequestsFragment", "Filtering by state: Completed")
                        filterPedidosByState("Completed")
                    }
                    R.id.not_approved -> {
                        Log.d("RequestsFragment", "Filtering by state: In Progress")
                        filterPedidosByState("In Progress")
                    }
                }
                true
            }
            popupMenu.show()
        } catch (e: Exception) {
            Log.e("RequestsFragment", "Error showing filter options", e)
        }
    }

    private fun filterPedidosByState(state: String) {
        // Filtra os pedidos com base no estado selecionado
        val filteredPedidos = pedidos.filter { it.state == state }
        // Atualiza o RecyclerView apenas com os pedidos filtrados

        // Atualiza o texto da TextView para exibir o estado selecionado
        view?.findViewById<TextView>(R.id.textViewStateHeader)?.text = state
    }
}
