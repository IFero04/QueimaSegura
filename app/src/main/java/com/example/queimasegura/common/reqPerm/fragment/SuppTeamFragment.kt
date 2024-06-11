package com.example.queimasegura.common.reqPerm.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.example.queimasegura.R

class SuppTeamFragment : Fragment() {

    interface OnSuppTeamSelectedListener {
        fun onSuppTeamSelected(suppTeam: String)
    }

    private var listener: OnSuppTeamSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSuppTeamSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSuppTeamSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_supp_team, container, false)

        view.findViewById<ImageButton>(R.id.imageButtonDropDownSuppTeam).setOnClickListener {
            handleDropDownMenu(it, R.menu.temp_dropdown_supp_team)
        }

        return  view
    }

    private fun handleDropDownMenu(view: View, menuId: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            listener?.onSuppTeamSelected(item.title.toString())
            true
        }

        popupMenu.show()
    }

    private fun showToast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }
}