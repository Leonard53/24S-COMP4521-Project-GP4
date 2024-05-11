package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comp4521_project_gp4.R

class AddExerciseActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Make the status bar transparent
    setContentView(R.layout.activity_add_exercise);
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
  }
}