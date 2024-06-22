package com.example.queimasegura.common.fire.search

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.fire.CreateFireActivity
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.repository.Repository

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: ArrayAdapter<String>

    private var locations: List<Location> = listOf()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewModels()

        initEvents()

        initListeners()

        initObservers()
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = SearchViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private fun initEvents() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        val listView = findViewById<ListView>(R.id.suggestions_list)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        viewModel.getLocation(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initListeners() {
        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            finish()
        }

        findViewById<ListView>(R.id.suggestions_list).setOnItemClickListener { _, _, position, _ ->
            val selectedLocation = locations[position]
            val intent = Intent(this, CreateFireActivity::class.java).apply {
                putExtra("LOCATION_ID", selectedLocation.id)
                putExtra("ZIP_CODE", selectedLocation.zipCode)
            }
            startActivity(intent)
        }
    }

    private fun initObservers() {
        viewModel.locationResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let { location ->
                    locations = location.result
                    updateListView(location.result)
                }
            } else {
                showMessage("Error: ${response.errorBody()?.string()}")
            }
        })
    }

    private fun updateListView(locations: List<Location>) {
        val locationStrings = locations.map { location ->
            val locationStringBuilder = StringBuilder()

            locationStringBuilder.append(location.zipCode)
            locationStringBuilder.append(", ")
            locationStringBuilder.append(location.locationName)

            if (!location.artName.isNullOrEmpty()) {
                locationStringBuilder.append(" - ")
                locationStringBuilder.append(location.artName)
            }

            if (!location.tronco.isNullOrEmpty()) {
                locationStringBuilder.append(" - ")
                locationStringBuilder.append(location.tronco)
            }
            locationStringBuilder.toString()
        }

        adapter.clear()
        adapter.addAll(locationStrings)
        adapter.notifyDataSetChanged()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}