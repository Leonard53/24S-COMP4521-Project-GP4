package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.model.ExerciseModel
import com.comp4521_project_gp4.model.FriendsModel
import kotlinx.coroutines.launch

class ExerciseViewModel : ViewModel() {
  // TODO: Implement the ViewModel
  private val _exercises = MutableLiveData<List<ExerciseModel.ExerciseItem>>()
  val exercises: LiveData<List<ExerciseModel.ExerciseItem>> = _exercises
  private val currentUser = MutableLiveData<User>()
  
  fun setUser(user: User) {
    currentUser.value = user
    viewModelScope.launch {
      loadExercises()
    }
  }
  
  init {
    viewModelScope.launch {
      loadExercises()
    }
  }
  
  private fun loadExercises() {
    // Simulate loading data
    val exerciseList = mutableListOf<ExerciseModel.ExerciseItem>()
    
    if (!currentUser.isInitialized || currentUser.value == null) {
//      ExerciseModel.ExerciseItem("Running", "30 minutes at park", 234)
    } else {
      (currentUser.value?.getCurrentUserExerciseCache() ?: emptyList()).forEach { activityRecord ->
        exerciseList.add(
          ExerciseModel.ExerciseItem(
            activityRecord.exerciseName,
            activityRecord.date,
            activityRecord.exerciseLengthInMins.toInt(),
            activityRecord.calories.toInt(),
            activityRecord.latitude,
            activityRecord.longitude
          )
        )
      }
    }
    
    _exercises.value = exerciseList
  }
}