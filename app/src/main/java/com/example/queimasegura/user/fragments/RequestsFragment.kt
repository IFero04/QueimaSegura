package com.example.queimasegura.user.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.common.Pedido
import com.example.queimasegura.common.PedidoAdapter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RequestsFragment : Fragment() {
    private lateinit var date: Date
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests_u, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val pedidos = listOf(
            Pedido("Queima", "2024-06-11", "Pending"),
            Pedido("Queimada", "2024-06-10", "Completed"),
            Pedido("Queima", "2024-06-09", "In Progress"),
            Pedido("Queima", "2024-06-08", "Pending"),
            Pedido("Queima", "2024-06-07", "Completed"),
            Pedido("Queimada", "2024-06-06", "In Progress"),
            Pedido("Queima", "2024-06-05", "Pending"),
            Pedido("Queima", "2024-06-04", "Completed"),
            Pedido("Queimada", "2024-06-03", "In Progress"),
            Pedido("Queima", "2024-06-02", "Pending"),
            Pedido("Queima", "2024-06-01", "Completed")
        ).sortedByDescending { it.date }

        recyclerView.adapter = PedidoAdapter(requireContext(), pedidos)


        return view
    }



    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            date = dateFormat.parse(selectedDate) ?: Date()
            showToast(selectedDate)
        },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }
}
