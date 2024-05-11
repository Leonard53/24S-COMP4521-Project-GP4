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
import com.google.android.material.card.MaterialCardView
import com.comp4521_project_gp4.R

class MainActivity : AppCompatActivity() {
  private lateinit var menuButton: ImageView
  private lateinit var currentUser: User
  private val mainViewModel: MainViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Make the status bar transparent
    window.statusBarColor = Color.TRANSPARENT
    
    // Make the navigation bar transparent (optional)
    window.navigationBarColor = Color.TRANSPARENT
    
    // Ensure content is drawn behind the status and navigation bars
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    setContentView(R.layout.activity_main)
    
    val loggedInUser = intent.getParcelableExtra<User>("user")
    currentUser = loggedInUser!!

    loggedInUser.let {
      mainViewModel.setUser(it)
    } ?: run {
      // Handle case where no User object was passed in Intent
      // Perhaps close the activity or show an error
    }
    
    val user_name = findViewById<TextView>(R.id.user_name);
    val calories_burned = findViewById<TextView>(R.id.info1_value);
    val calories_intake = findViewById<TextView>(R.id.info2_value);
    val exercise_time = findViewById<TextView>(R.id.info3_value);
    val exercise_btn: MaterialCardView = findViewById(R.id.main_exercise_btn);
    val nutrition_btn: MaterialCardView = findViewById(R.id.main_nutrition_btn);
    val leadboard_btn: MaterialCardView = findViewById(R.id.main_leaderboard_btn);
    val friends_btn: MaterialCardView = findViewById(R.id.main_friend_btn)
    
    user_name.text = currentUser.getUsername();
    
    // Observe ViewModel
    mainViewModel.caloriesBurned.observe(this) { burnedCalories ->
      calories_burned.text = burnedCalories.toString()
    }
    
    mainViewModel.caloriesIntake.observe(this) { intakeCalories ->
      calories_intake.text = intakeCalories.toString()
    }
    
    mainViewModel.exerciseTime.observe(this) { timeInMinutes ->
      exercise_time.text = timeInMinutes.toString()
    }
    
    menuButton = findViewById<ImageView>(R.id.menuButton)
    menuButton.setOnClickListener {
      showPopupMenu(it)
    }
    
    exercise_btn.setOnClickListener {
      // Create an Intent to start the Exercise Activity
      val intent = Intent(this, ExerciseActivity::class.java)
      startActivity(intent)
    }
    
    nutrition_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, AddFood::class.java)
      intent.putExtra("user", currentUser)
      startActivity(intent)
    }
    
    leadboard_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, DashboardActivity::class.java)
      startActivity(intent)
    }
    
    friends_btn.setOnClickListener {
      val intent = Intent(this, FriendsActivity::class.java)
      intent.putExtra("user", currentUser)
      startActivity(intent)
    }

    mainViewModel.mainScreenOnLoad()
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