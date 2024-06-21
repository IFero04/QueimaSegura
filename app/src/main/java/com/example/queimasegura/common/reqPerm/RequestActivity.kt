package com.example.queimasegura.common.reqPerm

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.reqPerm.fragment.QueimadaFragment
import com.example.queimasegura.common.reqPerm.search.SearchActivity
import com.example.queimasegura.retrofit.repository.Repository
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RequestActivity : AppCompatActivity() {
    private lateinit var viewModel: RequestViewModel

    private lateinit var postCode: TextView
    private var postCodeId: Int = 0

    private lateinit var type: String
    private lateinit var motive: String
    private lateinit var date: Date
    private var latitude: Double? = null
    private var longitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_request)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permissionRequestPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnListeners()
        initVariables()
        getCoords()
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = RequestViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RequestViewModel::class.java]
    }

    private fun getCoords() {
        latitude = intent.getDoubleExtra("latitude", Double.NaN)
        longitude = intent.getDoubleExtra("longitude", Double.NaN)
    }

    private fun initVariables() {
        val intent = intent

        postCode = findViewById(R.id.textViewPostCode)
        postCodeId = intent.getIntExtra("LOCATION_ID", -1)

        val zipCode = intent.getStringExtra("ZIP_CODE")
        if (!zipCode.isNullOrEmpty()) postCode.text = zipCode



    }

    private fun btnListeners() {
        findViewById<RadioGroup>(R.id.radioGroupType).setOnCheckedChangeListener { _, checkedId ->
            handleTypeRadioGroupChange(checkedId)
        }

        findViewById<Button>(R.id.buttonMap).setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonPostCode).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.imageButtonDropDownMotive).setOnClickListener {
            handleDropDownMotiveMenu(it, R.menu.temp_dropdown_motive)
        }

        findViewById<ImageButton>(R.id.imageButtonDate).setOnClickListener {
            showDatePickerDialog()
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            cancelRequest()
        }
    }
    private fun cancelRequest() {
        finish()
    }
    private fun handleTypeRadioGroupChange(checkedId: Int) {
        when (checkedId) {
            R.id.radioButtonQueima -> type = R.id.radioButtonQueima.toString()
            R.id.radioButtonQueimada -> type = R.id.radioButtonQueimada.toString()
        }
    }

    private fun handleDropDownMotiveMenu(view: View, menuId: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            motive = item.title.toString()
            true
        }

        popupMenu.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
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
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    private fun removeFragment(containerId: Int) {
        val fragment = supportFragmentManager.findFragmentById(containerId)
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }
}
