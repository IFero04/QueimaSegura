package com.example.queimasegura.common.fire.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.fire.CreateFireActivity
import com.example.queimasegura.common.fire.model.LocationIntent
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.util.ApiUtils


class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: ArrayAdapter<String>

    private var locations: List<Location> = listOf()
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val searchDelay: Long = 500

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewModels()

        initEvents()

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
                    if(query.isNotEmpty()) {
                        viewModel.getLocation(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    newText?.let {
                        if (it.isNotEmpty()) {
                            viewModel.getLocation(it)
                        }
                    }
                }
                handler.postDelayed(searchRunnable!!, searchDelay)
                return true
            }
        })

        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            finish()
        }

        findViewById<ListView>(R.id.suggestions_list).setOnItemClickListener { _, _, position, _ ->
            val location = locations[position]
            val locationIntent = LocationIntent(
                id = location.id,
                locationName = location.locationName,
                zipCode = location.zipCode,
                artName = location.artName,
                tronco = location.tronco
            )
            val intent = Intent(this, CreateFireActivity::class.java)
            intent.putExtra("selectedLocation", locationIntent)
            startActivity(intent)
            finish()
        }
    }

    private fun initObservers() {
        viewModel.locationResponse.observe(this) { response ->
            if (response.isSuccessful) {
                response.body()?.let { location ->
                    locations = location.result
                    updateListView(location.result)
                }
            } else if(response.errorBody() != null) {
                ApiUtils.handleApiError(application, response.errorBody(), ::showMessage)
            } else{
                showMessage(application.getString(R.string.server_error))
            }
        }
    }

    private fun updateListView(locations: List<Location>) {
        val locationStrings = locations.map { location ->
            val locationStringBuilder = StringBuilder()

            locationStringBuilder.append(location.zipCode)
            locationStringBuilder.append(", ")
            locationStringBuilder.append(location.locationName)

            location.artName?.let {
                if (it.isNotEmpty()) {
                    locationStringBuilder.append(" - ")
                    locationStringBuilder.append(it)
                }
            }

            location.tronco?.let {
                if (it.isNotEmpty()) {
                    locationStringBuilder.append(" - ")
                    locationStringBuilder.append(it)
                }
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
