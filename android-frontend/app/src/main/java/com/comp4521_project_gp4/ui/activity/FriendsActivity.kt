package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.model.FriendsModel
import com.comp4521_project_gp4.ui.adapters.FriendsAdapter
import com.comp4521_project_gp4.viewmodel.FriendViewModel
import kotlinx.coroutines.launch

class FriendsActivity : AppCompatActivity() {
  private lateinit var viewModel: FriendViewModel
  private val friendList = mutableListOf<String>()
  private lateinit var currentUser: User
  private lateinit var recyclerViewAdapter: ArrayAdapter<FriendsModel.FriendsItem>
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Make the status bar transparent
    setContentView(R.layout.activity_friends)
    viewModel = ViewModelProvider(this)[FriendViewModel::class.java]
    currentUser = intent.getParcelableExtra<User>("user")!!
    viewModel.setCurrentUser(currentUser)
    
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
    val context = this
    searchButton.setOnClickListener {
      val userId = userIdEditText.text.toString()
      lifecycleScope.launch {
        try {
          currentUser.addFriend(userId)
          viewModel.loadFriends()
        } catch (e: Exception) {
          val exceptionDialog =
            AlertDialog.Builder(context)
              .setMessage(e.toString())
              .setMessage("User not found")
              .setNegativeButton("OK", null)
              .create()
          exceptionDialog.show()
        }
      }
      dialog.dismiss()
    }
    // Show the dialog
    dialog.show()
  }
}