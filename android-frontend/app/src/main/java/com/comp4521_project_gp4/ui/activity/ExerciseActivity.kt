package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.ui.adapters.ExerciseAdapter
import com.comp4521_project_gp4.viewmodel.ExerciseViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton


class ExerciseActivity : AppCompatActivity() {
  private val viewModel: ExerciseViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exercise)
    
    val recyclerView: RecyclerView = findViewById(R.id.exercise_recyclerView)
    // Initialize the adapter with the data
    val adapter = ExerciseAdapter(emptyList())
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    // Observe ViewModel
    viewModel.exercises.observe(this) { exercises ->
      // Update adapter data
      adapter.updateData(exercises)
    }
    
    // Setting up the button click listener
    val addButton: MaterialButton = findViewById(R.id.addExerciseBtn)
    addButton.setOnClickListener {
      val intent = Intent(this, AddExerciseActivity::class.java)
      startActivity(intent)
    }
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commitNow()  // Using commitNow to wait for completion
      setToolbarTitle("Activity History")
    }
  }
  
  private fun setToolbarTitle(title: String) {
    val fragment = supportFragmentManager.findFragmentById(R.id.toolbar_container) as? ToolbarFragment
    fragment?.setToolbarTitle(title)
  }
}