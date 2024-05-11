package com.comp4521_project_gp4.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Intent
//import com.example.comp4521_project_gp4.ui.theme.COMP4521ProjectGP4Theme
import com.comp4521_project_gp4.R
import android.graphics.Color
import android.view.View
import com.google.android.material.card.MaterialCardView


class MainActivity : ComponentActivity() {
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

        val calories_burned = findViewById<TextView>(R.id.info1_value);
        val calories_intake = findViewById<TextView>(R.id.info2_value);
        val exercise_time = findViewById<TextView>(R.id.info3_value);

        val exercise_btn: MaterialCardView = findViewById(R.id.main_exercise_btn);
        val nutrition_btn: MaterialCardView = findViewById(R.id.main_nutrition_btn);
        val leadboard_btn: MaterialCardView = findViewById(R.id.main_leaderboard_btn);

        calories_burned.text = "1020";
        calories_intake.text = "1100";
        exercise_time.text = "162";

        exercise_btn.setOnClickListener {
            // Create an Intent to start the Exercise Activity
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
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
}