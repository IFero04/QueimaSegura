package com.example.queimasegura.common.reqPerm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.queimasegura.R
import com.example.queimasegura.common.login.LoginActivity
import com.example.queimasegura.common.reqPerm.fragment.MapFragment
import com.example.queimasegura.common.reqPerm.fragment.PostCodeFragment
import com.example.queimasegura.common.reqPerm.fragment.QueimadaFragment
import com.example.queimasegura.common.reqPerm.fragment.SuppTeamFragment

class RequestActivity : AppCompatActivity() {
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
    }

    private fun btnListeners() {
        findViewById<RadioGroup>(R.id.radioGroupType).setOnCheckedChangeListener { _, checkedId ->
            handleTypeRadioGroupChange(checkedId)
        }

        findViewById<RadioGroup>(R.id.radioGroupSuppTeam).setOnCheckedChangeListener { _, checkedId ->
            handleSuppTeamRadioGroupChange(checkedId)
        }

        findViewById<RadioGroup>(R.id.radioGroupLocation).setOnCheckedChangeListener { _, checkedId ->
            handleLocationRadioGroupChange(checkedId)
        }

        findViewById<ImageButton>(R.id.imageButtonBack).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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

    private fun handleLocationRadioGroupChange(checkedId: Int) {
        val fragment = when (checkedId) {
            R.id.radioButtonPostCode -> PostCodeFragment()
            R.id.radioButtonMap -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                null
            }
            else -> null
        }

        if (fragment != null) {
            replaceFragment(fragment, R.id.fragmentContainerViewLocation)
        } else {
            removeFragment(R.id.fragmentContainerViewLocation)
        }
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
