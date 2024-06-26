package com.example.queimasegura.admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.queimasegura.R
import com.example.queimasegura.admin.model.User
import com.example.queimasegura.admin.adapters.UserAdapter
import com.example.queimasegura.admin.model.Rule

class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private var isPermissionMode: Boolean = false
    private var isDeleteMode = false
    private var isBanMode = false

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

        val buttonAdd: Button = view.findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            showAddRuleFragment()
        }

        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            isDeleteMode = !isDeleteMode
            buttonDelete.setText(R.string.cancel_btn)
            Toast.makeText(requireContext(), "Select an item to delete", Toast.LENGTH_SHORT).show()
        }

        val buttonEditPerms: Button = view.findViewById(R.id.buttonEditUsers)
        buttonEditPerms.setOnClickListener {
            isPermissionMode = !isPermissionMode
        }

        if (isPermissionMode) Toast.makeText(requireContext(), "Select an item to edit", Toast.LENGTH_SHORT).show()

        val buttonBan: Button = view.findViewById(R.id.buttonBanUnban)
        buttonBan.setOnClickListener {
            isBanMode = !isBanMode
            buttonBan.setText(R.string.cancel_btn)
        }

        val filter: ImageButton = view.findViewById(R.id.imageButtonFilterUsers)
        filter.setOnClickListener {
            handleFilterMenu(filter, R.menu.filter_users_admin)
        }


        adapter = UserAdapter(userList) { position ->
            if (isPermissionMode) {
                showPermissionDialog(position)
            }
            if (isDeleteMode) {
                adapter.deleteRule(position)
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                buttonDelete.setText(R.string.delete_btn)
            }
            if (isBanMode) {
                Toast.makeText(requireContext(), "BANNED", Toast.LENGTH_SHORT).show()
                buttonBan.setText(R.string.ban_btn)
            }
        }
        recyclerView.adapter = adapter


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

    private fun showAddRuleFragment() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_add_user, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Add Rule")

        val dialog = dialogBuilder.create()
        dialog.show()

        dialogView.findViewById<ImageButton>(R.id.imageButtonDropDownType).setOnClickListener {
            handleDropDownMenu(it, R.menu.temp_dropdown_type, dialogView.findViewById(R.id.textViewType))
        }

        dialogView.findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
            val username: String = dialogView.findViewById<EditText>(R.id.editTextUsername).text.toString()
            val email: String = dialogView.findViewById<EditText>(R.id.editTextEmail).text.toString()
            val type: String = dialogView.findViewById<TextView>(R.id.textViewType).text.toString()

            val isValid = handleConfirmation(username, email, type)

            if (isValid) {
                val newUser = User(username, email, type)
                userList.add(newUser)
                adapter.notifyItemInserted(userList.size -1)
                dialog.dismiss()
            }
        }
    }

    private fun handleDropDownMenu(view: View, menuId: Int, textView: TextView) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            textView.text = item.title
            true
        }

        popupMenu.show()
    }

    private fun handleFilterMenu(view: View, menuId: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
            true
        }

        popupMenu.show()
    }



    private fun handleConfirmation(username: String, email: String, type: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || type.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields1", Toast.LENGTH_SHORT).show()
            return false
        }

        val emailExists = userList.any { user -> user.email == email }

        if (emailExists) {
            Toast.makeText(requireContext(), "Email already exists!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}