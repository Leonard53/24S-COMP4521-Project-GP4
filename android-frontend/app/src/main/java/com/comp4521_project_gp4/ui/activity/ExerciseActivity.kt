package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.ui.adapters.ExerciseAdapter
import com.comp4521_project_gp4.viewmodel.ExerciseViewModel
import com.google.android.material.button.MaterialButton

class ExerciseActivity : AppCompatActivity() {
  private val viewModel: ExerciseViewModel by viewModels()
  private lateinit var currentUser: User
  private lateinit var adapter: ExerciseAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exercise)
    
    currentUser = intent.getParcelableExtra<User>("user")!!
    viewModel.setUser(currentUser)
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commitNow()  // Using commitNow to wait for completion
    }
    
    val recyclerView: RecyclerView = findViewById(R.id.exercise_recyclerView)
    adapter = ExerciseAdapter(emptyList())
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    viewModel.exercises.observe(this) { exercises ->
      adapter.updateData(exercises)
    }
    
    val addButton: MaterialButton = findViewById(R.id.addExerciseBtn)
    addButton.setOnClickListener {
      val intent = Intent(this, AddExerciseActivity::class.java)
      intent.putExtra("user", currentUser)
      startActivity(intent)
    }
    
  }
  
  
  override fun onResume() {
    super.onResume()
    adapter.mapViewOnResume()
  }
  
  override fun onPause() {
    super.onPause()
    adapter.mapViewOnPause()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    adapter.mapViewOnDestroy()
  }
  
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    adapter.mapViewOnSaveInstanceState(outState)
  }
  
  override fun onLowMemory() {
    super.onLowMemory()
    adapter.mapViewOnLowMemory()
  }
}