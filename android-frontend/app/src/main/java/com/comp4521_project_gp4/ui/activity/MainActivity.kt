package com.comp4521_project_gp4.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.view.View
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
    
    val calories_burned = findViewById<TextView>(R.id.info1_value)
    val calories_intake = findViewById<TextView>(R.id.info2_value)
    val exercise_time = findViewById<TextView>(R.id.info3_value)
    
    val exercise_btn: MaterialCardView = findViewById(R.id.main_exercise_btn)
    val friends_btn: MaterialCardView = findViewById(R.id.main_friend_btn)
    menuButton = findViewById<ImageView>(R.id.menuButton)
    
    menuButton.setOnClickListener {
      showPopupMenu(it)
    }
    
    calories_burned.text = "1020"
    calories_intake.text = "1100"
    exercise_time.text = "162"
    
    exercise_btn.setOnClickListener {
      startActivity(Intent(this, ExerciseActivity::class.java))
    }
    
    friends_btn.setOnClickListener {
      startActivity(Intent(this, FriendsActivity::class.java))
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