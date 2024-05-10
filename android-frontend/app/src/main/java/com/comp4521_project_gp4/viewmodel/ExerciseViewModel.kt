package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.model.ExerciseModel

class ExerciseViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _exercises = MutableLiveData<List<ExerciseModel.ExerciseItem>>()
    val exercises: LiveData<List<ExerciseModel.ExerciseItem>> = _exercises

    init {
        loadExercises()
    }

    private fun loadExercises() {
        // Simulate loading data
        val exerciseList = listOf(
            ExerciseModel.ExerciseItem("Running", "30 minutes at park", 234),
            ExerciseModel.ExerciseItem("Swimming and drinking haha diu", "1 hour in a pool", 561),
            // Add more items as needed
        )
        _exercises.value = exerciseList
    }
}