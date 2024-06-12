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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.queimasegura.R
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.common.reqPerm.fragment.QueimadaFragment
import com.example.queimasegura.common.reqPerm.fragment.SuppTeamFragment
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RequestActivity : AppCompatActivity(), SuppTeamFragment.OnSuppTeamSelectedListener {
    private lateinit var type: String
    private lateinit var motive: String
    private lateinit var date: Date
    private lateinit var suppTeam: String
    private lateinit var postCode: String
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
        getCoords()
    }

    private fun getCoords() {
        latitude = intent.getDoubleExtra("latitude", Double.NaN)
        longitude = intent.getDoubleExtra("longitude", Double.NaN)
    }

    private fun btnListeners() {
        findViewById<RadioGroup>(R.id.radioGroupType).setOnCheckedChangeListener { _, checkedId ->
            handleTypeRadioGroupChange(checkedId)
        }

        findViewById<RadioGroup>(R.id.radioGroupSuppTeam).setOnCheckedChangeListener { _, checkedId ->
            handleSuppTeamRadioGroupChange(checkedId)
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButtonDropDownMotive).setOnClickListener {
            handleDropDownMotiveMenu(it, R.menu.temp_dropdown_motive)
        }

        findViewById<ImageButton>(R.id.imageButtonDate).setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun handleTypeRadioGroupChange(checkedId: Int) {
        val fragment = when (checkedId) {
            R.id.radioButtonQueimada -> QueimadaFragment()
            else -> null
        }

        if (fragment != null) {
            replaceFragment(fragment, R.id.fragmentContainerViewType)
        } else {
            removeFragment(R.id.fragmentContainerViewType)
        }

        when (checkedId) {
            R.id.radioButtonQueima -> type = R.id.radioButtonQueima.toString()
            R.id.radioButtonQueimada -> type = R.id.radioButtonQueimada.toString()
        }
    }

    private fun handleSuppTeamRadioGroupChange(checkedId: Int) {
        val fragment = when (checkedId) {
            R.id.radioButtonYes -> SuppTeamFragment()
            else -> null
        }

        if (fragment != null) {
            replaceFragment(fragment, R.id.fragmentContainerViewSuppTeam)
        } else {
            removeFragment(R.id.fragmentContainerViewSuppTeam)
        }
    }

    override fun onSuppTeamSelected(suppTeam: String) {
        this.suppTeam = suppTeam
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
