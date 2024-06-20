package com.example.queimasegura.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.queimasegura.R
import com.example.queimasegura.common.Pedido
import com.example.queimasegura.common.reqPerm.RequestActivity

class HomeFragment : Fragment() {

    private lateinit var typeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var stateTextView: TextView
    private lateinit var cardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_u, container, false)

        // Initialize views
        typeTextView = view.findViewById(R.id.type)
        dateTextView = view.findViewById(R.id.date)
        stateTextView = view.findViewById(R.id.state)
        cardView = view.findViewById(R.id.cardView)

        // Simulate list of orders (replace with your actual logic)
        val orders = listOf(
            Pedido("Queima", "2024-06-11", "Pending"),
            Pedido("Queimada", "2024-06-12", "Completed"),
            Pedido("Queima", "2024-06-09", "In Progress")
        )

        // Find the most recent order based on date
        val mostRecentOrder = orders.maxByOrNull { it.date }

        // Display the most recent order in the card
        mostRecentOrder?.let {
            typeTextView.text = it.type
            dateTextView.text = it.date
            stateTextView.text = it.state
            cardView.visibility = View.VISIBLE
        }

        // Hide the card if no orders are present
        if (orders.isEmpty()) {
            cardView.visibility = View.GONE
        }

        // Handle click on addImageView
        val addImageView = view.findViewById<ImageView>(R.id.add)
        addImageView.setOnClickListener {
            val intent = Intent(activity, RequestActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
