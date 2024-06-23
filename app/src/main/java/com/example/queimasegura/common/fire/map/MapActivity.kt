package com.example.queimasegura.common.fire.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.fire.CreateFireActivity
import com.example.queimasegura.common.fire.model.ZipcodeIntent
import com.example.queimasegura.retrofit.model.data.Location
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.util.ApiUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var userMarker: Marker? = null
    private val portugalBounds = LatLngBounds(
        LatLng(36.9614, -9.5000),
        LatLng(42.1543, -6.1892)
    )
    private var coords: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reqPermsMapPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initViewModels()

        initEvents()
    }

    private fun initViewModels(){
        val repository = Repository()
        val mapModelFactory = MapViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, mapModelFactory)[MapViewModel::class.java]
    }

    private fun initEvents() {
        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, CreateFireActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
            coords?.let {
                viewModel.getMapLocation(it.latitude, it.longitude, ::handleSendLocation)
            } ?: run {
                showMessage(getString(R.string.coords_not_selected_toast))
            }
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(portugalBounds, 0))
        googleMap.setLatLngBoundsForCameraTarget(portugalBounds)
        googleMap.setMinZoomPreference(6.0f)
        googleMap.setMaxZoomPreference(25.0f)
        googleMap.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        if (portugalBounds.contains(latLng)) {
            userMarker?.remove()
            userMarker = googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            coords = latLng
        } else {
            showMessage(getString(R.string.marker_boundaries_toast))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun handleSendLocation(location: Location) {
        val zipcodeIntent = ZipcodeIntent(
            id = location.id,
            locationName = location.locationName,
            zipCode = location.zipCode,
            artName = location.artName,
            tronco = location.tronco
        )
        val intent = Intent(this, CreateFireActivity::class.java)
        intent.putExtra("selectedZipcode", zipcodeIntent)
        startActivity(intent)
        finish()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}