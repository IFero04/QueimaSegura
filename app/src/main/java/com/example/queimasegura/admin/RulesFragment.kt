package com.example.queimasegura.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.admin.adapters.RuleAdapter
import com.example.queimasegura.admin.model.Rule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RulesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RuleAdapter
    private lateinit var rulesList: List<Rule>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rules, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewRulesTable)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val sampleDate = getTodayDate();

        rulesList = List(15) { index ->
            Rule("District$index", sampleDate, sampleDate)
        }

        adapter = RuleAdapter(rulesList)
        recyclerView.adapter = adapter

        return view
    }

    private fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val today = Date()

        return dateFormat.format(today)
    }
}