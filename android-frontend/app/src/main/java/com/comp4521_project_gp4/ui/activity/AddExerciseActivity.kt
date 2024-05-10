package com.comp4521_project_gp4.ui.activity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Intent
import com.comp4521_project_gp4.R
import android.graphics.Color
import android.view.View
import com.google.android.material.card.MaterialCardView
class AddExerciseActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Make the status bar transparent
    setContentView(R.layout.activity_add_exercise);
  }
}