package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import com.comp4521_project_gp4.R
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.ui.adapters.FriendsAdapter
import com.comp4521_project_gp4.viewmodel.FriendViewModel

class FriendsActivity : AppCompatActivity() {
  private val viewModel: FriendViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Make the status bar transparent
    setContentView(R.layout.activity_friends);
    
    val recyclerView: RecyclerView = findViewById(R.id.friendsRecyclerView)
    val adapter = FriendsAdapter(emptyList())
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    val addFriendBtn: Button = findViewById(R.id.addFriendButton)
    
    addFriendBtn.setOnClickListener {
      showInputDialog()
    }
    
    // Observe ViewModel
    viewModel.friends.observe(this) { exercises ->
      // Update adapter data
      adapter.updateData(exercises)
    }
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
  }
  
  private fun showInputDialog() {
    // Inflate the custom layout
    val dialogView = layoutInflater.inflate(R.layout.friend_popup, null)
    val userIdEditText = dialogView.findViewById<EditText>(R.id.userIdEditText)
    val searchButton = dialogView.findViewById<Button>(R.id.searchButton)
    
    // Create the dialog
    val dialog = AlertDialog.Builder(this)
      .setTitle("Input User ID")
      .setView(dialogView)
      .setNegativeButton("Cancel", null)
      .create()
    
    // Handle the search button click within the dialog
    searchButton.setOnClickListener {
      val userId = userIdEditText.text.toString()
      dialog.dismiss()
    }
    
    // Show the dialog
    dialog.show()
  }
}