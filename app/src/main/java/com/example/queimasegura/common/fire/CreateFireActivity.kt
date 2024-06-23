package com.example.queimasegura.common.fire

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.queimasegura.R
import com.example.queimasegura.common.fire.adapter.ReasonsAdapter
import com.example.queimasegura.common.fire.map.MapActivity
import com.example.queimasegura.common.fire.model.ZipcodeIntent
import com.example.queimasegura.common.fire.search.SearchActivity
import com.example.queimasegura.retrofit.repository.Repository
import com.example.queimasegura.room.entities.Reason
import com.example.queimasegura.room.entities.Type
import java.util.Calendar


class CreateFireActivity : AppCompatActivity() {
    private lateinit var viewModel: CreateFireViewModel
    private lateinit var spinnerReason: Spinner
    private lateinit var reasonsAdapter: ReasonsAdapter
    private lateinit var radioGroupType: RadioGroup

    private lateinit var zipcodeData: ZipcodeIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        initViewModels()

        initIntents()

        initVariables()

        initEvents()

        initObservers()
    }

    private fun initViewModels() {
        val repository = Repository()
        val viewModelFactory = CreateFireViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[CreateFireViewModel::class.java]
    }

    private fun initIntents() {
        val zipcodeIntent = intent.getParcelableExtra<ZipcodeIntent>("selectedZipcode")
        zipcodeIntent?.let {
            zipcodeData = it
            handleLocationShow(it)
        }
    }

    private fun initVariables() {
        spinnerReason = findViewById(R.id.spinnerReason)
        reasonsAdapter = ReasonsAdapter(this, mutableListOf())
        radioGroupType = findViewById(R.id.radioGroupType)

        viewModel.typesData.observe(this) { types ->
            types?.let{
                populateRadioGroup(it)
            }
        }

        viewModel.reasonsData.observe(this) { reasons ->
            reasons?.let {
                populateReasons(it)
            }
        }
    }

    private fun initEvents() {
        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.buttonPostCode).setOnClickListener {
            popUp(SearchActivity::class.java)
        }

        findViewById<Button>(R.id.buttonMap).setOnClickListener {
            popUp(MapActivity::class.java)
        }

        findViewById<RadioGroup>(R.id.radioGroupType).setOnCheckedChangeListener{ group, checkedId ->
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                if (radioButton.id == checkedId) {
                    radioButton.setTextColor(resources.getColor(R.color.black))
                } else {
                    radioButton.setTextColor(resources.getColor(android.R.color.darker_gray))
                }
            }
        }

        findViewById<ImageButton>(R.id.imageButtonDate).setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun initObservers() {

    }

    private fun handleLocationShow(location: ZipcodeIntent) {
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

        val locationOutput = findViewById<TextView>(R.id.textViewOutputLocation)
        locationOutput.text = locationStringBuilder
    }

    private fun populateRadioGroup(types: List<Type>) {
        val radioGroupType = findViewById<RadioGroup>(R.id.radioGroupType)
        radioGroupType.removeAllViews()
        var checkedId = -1

        for (type in types) {
            val radioButton = RadioButton(this).apply {
                id = type.id
                text = type.nameEn
                textSize = 16f
                setTextColor(resources.getColor(R.color.black, null))
                buttonTintList = ContextCompat.getColorStateList(context, R.color.radio_btn_tint)
                typeface = ResourcesCompat.getFont(this@CreateFireActivity, R.font.karma_medium)
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    radioGroupType.check(id)
                }
            }
            radioGroupType.addView(radioButton)

            if (checkedId == -1) {
                radioButton.isChecked = true
                checkedId = radioButton.id
            }
        }
    }

    private fun populateReasons(reasons: List<Reason>) {
        reasonsAdapter.clear()
        reasonsAdapter.addAll(reasons)
        spinnerReason.adapter = reasonsAdapter
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

            val textViewDate = findViewById<TextView>(R.id.textViewDateShow)
            val dateString = getString(R.string.create_fire_date) + " " + selectedDate
            textViewDate.text = dateString
        },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    companion object {
        const val REQUEST_CODE = 123 // Use the same value here and when starting activities for result
    }

    private fun popUp(destination: Class<*>) {
        startActivity(Intent(this, destination))
    }

    private fun showMessage(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }
}
