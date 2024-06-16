package com.example.queimasegura.admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.admin.model.User
import com.example.queimasegura.admin.adapters.UserAdapter

class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: List<User>
    private var isPermissionMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUserTable)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        userList = MutableList(15) { index ->
            User("Username$index", "email$index@example.com", "Manager")
        }

        adapter = UserAdapter(userList) { position ->
            if (isPermissionMode) {
                showPermissionDialog(position)
            }
        }
        recyclerView.adapter = adapter

        val buttonPermissions: Button = view.findViewById(R.id.buttonPermissions)
        buttonPermissions.text = getString(R.string.users_edit_btn)
        buttonPermissions.setOnClickListener {
            isPermissionMode = !isPermissionMode
            buttonPermissions.text = if (isPermissionMode) {
                getString(R.string.leave_edit_mode)
            } else {
                getString(R.string.users_edit_btn)
            }
        }

        return view
    }

    private fun showPermissionDialog(position: Int) {
        val user = userList[position]

        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_edit_perms, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit Permissions")

        val dialog = dialogBuilder.create()
        dialog.show()

        val radioGroup: RadioGroup = dialogView.findViewById(R.id.radioGroup)
        val radioButtonAdmin: RadioButton = dialogView.findViewById(R.id.radioButtonAdmin)
        val radioButtonManager: RadioButton = dialogView.findViewById(R.id.radioButtonManager)
        val radioButtonUser: RadioButton = dialogView.findViewById(R.id.radioButtonUser)

        when (user.type) {
            "Admin" -> radioButtonAdmin.isChecked = true
            "Manager" -> radioButtonManager.isChecked = true
            "User" -> radioButtonUser.isChecked = true
        }

        val confirmButton: Button = dialogView.findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val selectedRadioButton: RadioButton = dialogView.findViewById(selectedRadioButtonId)
            val selectedPermission = selectedRadioButton.text.toString()

            adapter.updateUserPermission(position, selectedPermission)
            dialog.dismiss()
        }
    }
}