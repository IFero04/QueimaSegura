package com.example.queimasegura.user.fragments.fire

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.Pedido
import com.example.queimasegura.common.PedidoAdapter
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.entities.Fire
import com.example.queimasegura.user.fragments.fire.adapter.FireAdapter

class FiresFragment : Fragment() {
    private lateinit var viewModel: FiresViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var fireAdapter: FireAdapter
    private lateinit var fires: List<Fire>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModels()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests_u, container, false)

        initVariables(view)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val imageButtonFilter = view.findViewById<ImageButton>(R.id.imageButtonDate)
        imageButtonFilter.setOnClickListener { showFilterOptions(it) }


        fireAdapter = FireAdapter(requireContext(), fires)
        recyclerView.adapter = fireAdapter

        return view
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = FiresViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[FiresViewModel::class.java]
    }

    private fun initVariables(view: View) {
        viewModel.firesData.observe(viewLifecycleOwner) { firesData ->
            firesData?.let {
                fires = it
            }
        }


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
                    R.id.completed -> {
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
        val filteredPedidos = fires.filter { it.status == state }
        // Atualiza o RecyclerView apenas com os pedidos filtrados

        // Atualiza o texto da TextView para exibir o estado selecionado
        view?.findViewById<TextView>(R.id.textViewStateHeader)?.text = state
    }
}
