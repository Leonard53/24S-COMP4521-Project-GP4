package com.comp4521_project_gp4.ui.activity

import MainViewModel
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.comp4521_project_gp4.backend.aws.User
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.comp4521_project_gp4.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  
  private lateinit var menuButton: ImageView
  private lateinit var currentUser: User
  private val mainViewModel: MainViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Setup UI and other initializations
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    setContentView(R.layout.activity_main)
    
    // Initialize components and setup listeners
    initializeUI()
  }
  
  override fun onResume() {
    super.onResume()
    // This will ensure mainScreenOnLoad is called every time the activity resumes
    mainViewModel.mainScreenOnLoad()
    
    lifecycleScope.launch {
      uiComponentUpdate()
    }
  }
  
  private fun initializeUI() {
    
    val loggedInUser = intent.getParcelableExtra<User>("user")
    currentUser = loggedInUser!!
    
    loggedInUser.let {
      mainViewModel.setUser(it)
    } ?: run {
      // Handle case where no User object was passed in Intent
      finish()
    }
    
    lifecycleScope.launch {
      uiComponentUpdate()
    }
    setupButtons()
    setupMenu()
  }
  
  private suspend fun uiComponentUpdate() {
    // Setup UI components
    val user_name = findViewById<TextView>(R.id.user_name)
    val calories_burned = findViewById<TextView>(R.id.info1_value)
    val calories_intake = findViewById<TextView>(R.id.info2_value)
    val exercise_time = findViewById<TextView>(R.id.info3_value)
    
    user_name.text = currentUser.getUsername()
    
    mainViewModel.caloriesBurned.observe(this) { burnedCalories ->
      calories_burned.text = burnedCalories.toString()
    }
    mainViewModel.caloriesIntake.observe(this) { intakeCalories ->
      calories_intake.text = intakeCalories.toString()
    }
    mainViewModel.exerciseTime.observe(this) { timeInMinutes ->
      exercise_time.text = timeInMinutes.toString()
    }
  }
  
  private fun setupButtons() {
    val exercise_btn: MaterialCardView = findViewById(R.id.main_exercise_btn)
    val nutrition_btn: MaterialCardView = findViewById(R.id.main_nutrition_btn)
    val leadboard_btn: MaterialCardView = findViewById(R.id.main_leaderboard_btn)
    val friends_btn: MaterialCardView = findViewById(R.id.main_friend_btn)
    
    exercise_btn.setOnClickListener {
      startActivity(Intent(this, ExerciseActivity::class.java).apply {
        putExtra("user", currentUser)
      })
    }
    nutrition_btn.setOnClickListener {
      startActivity(Intent(this, AddFood::class.java).apply {
        putExtra("user", currentUser)
      })
    }
    leadboard_btn.setOnClickListener {
      startActivity(Intent(this, DashboardActivity::class.java))
    }
    friends_btn.setOnClickListener {
      val intent = Intent(this, FriendsActivity::class.java)
      intent.putExtra("user", currentUser)
      startActivity(intent)
    }
  }
  
  private fun setupMenu() {
    menuButton = findViewById<ImageView>(R.id.menuButton)
    menuButton.setOnClickListener {
      showPopupMenu(it)
    }
  }
  
  private fun showPopupMenu(view: View) {
    val popup = PopupMenu(this, view)
    popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
    popup.setOnMenuItemClickListener { menuItem ->
      when (menuItem.itemId) {
        R.id.menu_profile -> true
        R.id.menu_settings -> true
        R.id.menu_logout -> true
        else -> false
      }
    }
    popup.setOnDismissListener {
    // Reset icon to menu icon when the popup is dismissed
      menuButton.setImageResource(R.drawable.menu)
    }
    // Set icon to close icon when the popup is shown
    menuButton.setImageResource(R.drawable.close)
    popup.show()
  }
}