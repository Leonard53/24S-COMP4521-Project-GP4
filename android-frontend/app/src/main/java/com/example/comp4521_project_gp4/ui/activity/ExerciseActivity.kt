package com.example.comp4521_project_gp4.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.example.comp4521_project_gp4.R
import com.example.comp4521_project_gp4.model.ExerciseModel
import com.example.comp4521_project_gp4.ui.adapters.ExerciseAdapter
import com.example.comp4521_project_gp4.viewmodel.ExerciseViewModel


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
    }
}