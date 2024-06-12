package com.example.queimasegura.common.reqPerm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.queimasegura.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var userMarker: Marker? = null
    private val portugalBounds = LatLngBounds(
        LatLng(37.036253, -8.974492),
        LatLng(41.923624, -6.596450)
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

        btnListeners()
    }

    private fun btnListeners() {
        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
            coords?.let {
                val intent = Intent(this, RequestActivity::class.java)
                intent.putExtra("latitude", it.latitude)
                intent.putExtra("longitude", it.longitude)
                startActivity(intent)
            } ?: run {
                showToast(getString(R.string.coords_not_selected_toast))
            }
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(portugalBounds, 0))
        googleMap.setLatLngBoundsForCameraTarget(portugalBounds)
        googleMap.setMinZoomPreference(6.0f)
        googleMap.setMaxZoomPreference(15.0f)
        googleMap.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        if (portugalBounds.contains(latLng)) {
            userMarker?.remove()
            userMarker = googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            showToast(getString(R.string.marker_boundaries_toast))
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

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}