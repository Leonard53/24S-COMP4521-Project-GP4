package com.comp4521_project_gp4.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.comp4521_project_gp4.backend.aws_lambda.User
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.card.MaterialCardView
import com.comp4521_project_gp4.R

class MainActivity : AppCompatActivity() {
  private lateinit var menuButton: ImageView
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    setContentView(R.layout.activity_main)
    val loggedInUser = intent.getParcelableExtra<User>("user")
    println(loggedInUser?.getUsername())
    val calories_burned = findViewById<TextView>(R.id.info1_value)
    val calories_intake = findViewById<TextView>(R.id.info2_value)
    val exercise_time = findViewById<TextView>(R.id.info3_value)
    val exercise_btn: MaterialCardView = findViewById(R.id.main_exercise_btn)
    val nutrition_btn: MaterialCardView = findViewById(R.id.main_nutrition_btn);
    val leadboard_btn: MaterialCardView = findViewById(R.id.main_leaderboard_btn);
    val friends_btn: MaterialCardView = findViewById(R.id.main_friend_btn)
    menuButton = findViewById<ImageView>(R.id.menuButton)
    
    menuButton.setOnClickListener {
      showPopupMenu(it)
    }
    
    calories_burned.text = "1020"
    calories_intake.text = "1100"
    exercise_time.text = "162"
    
    exercise_btn.setOnClickListener {
      // Create an Intent to start the Exercise Activity
      val intent = Intent(this, ExerciseActivity::class.java)
      startActivity(intent)
    }
    
    nutrition_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, AddFood::class.java)
      intent.putExtra("user", loggedInUser)
      startActivity(intent)
    }
    
    leadboard_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, dashboard::class.java)
      startActivity(intent)
    }
    
    friends_btn.setOnClickListener {
      startActivity(Intent(this, FriendsActivity::class.java))
    }
    nutrition_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, AddFood::class.java)
      startActivity(intent)
    }
    
    leadboard_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, dashboard::class.java)
      startActivity(intent)
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