package com.comp4521_project_gp4.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comp4521_project_gp4.backend.aws.Exercise
import com.comp4521_project_gp4.backend.aws.User
import com.google.android.gms.maps.model.LatLng

class AddExerciseViewModel(application: Application) : AndroidViewModel(application) {
  private lateinit var currentUser: User
  
  private val _selectedLocation = MutableLiveData<LatLng>()
  val selectedLocation: LiveData<LatLng> = _selectedLocation
  
  private val _selectedDate = MutableLiveData<String>()
  val selectedDate: LiveData<String> = _selectedDate
  
  private val _selectedTime = MutableLiveData<String>()
  val selectedTime: LiveData<String> = _selectedTime
  
  private val _exerciseType = MutableLiveData<String>()
  val exerciseType: LiveData<String> = _exerciseType
  
  private val _calories = MutableLiveData<Int>()
  val exerciseCalories: LiveData<Int> = _calories
  
  private val _validationMessage = MutableLiveData<String?>()
  val validationMessage: LiveData<String?> = _validationMessage
  
  fun setCalories(calories: String) {
    _calories.value = calories.toInt()
  }
  
  fun setExerciseType(type: String) {
    _exerciseType.value = type
  }
  
  fun setLocation(location: LatLng) {
    _selectedLocation.value = location
  }
  
  fun setDate(date: String) {
    _selectedDate.value = date
  }
  
  fun setTime(time: String) {
    _selectedTime.value = time
  }
  
  fun clearValidationMessage() {
    _validationMessage.value = null
  }
  
  fun setUser(user: User) {
    currentUser = user
  }
  
  fun validateForm(): Boolean {
    // Check if the exercise type is empty or null
    if (exerciseType.value.isNullOrEmpty()) {
      _validationMessage.value = "Please select the type of exercise."
      return false
    }
    
    // Check if the date is empty or null
    if (selectedDate.value.isNullOrEmpty()) {
      _validationMessage.value = "Please select a date."
      return false
    }
    
    // Check if the time is empty or null
    if (selectedTime.value.isNullOrEmpty()) {
      _validationMessage.value = "Please select a time."
      return false
    }
    
    // Check if the location is not set
//    if (selectedLocation.value == null) {
//      _validationMessage.value = "Please select a location."
//      return false
//    }
    
    // Check if calories are not entered
    if (exerciseCalories.value == null) {
      _validationMessage.value = "Please enter the calories burned."
      return false
    }
    
    return true
  }
  
  suspend fun saveData() {
    // Parse the time to calculate total minutes
    val parts = selectedTime.value?.split(":")?.map { it.trim() }
    if (parts == null || parts.size != 2) {
      _validationMessage.value = "Invalid time format."
      return
    }
    val hours = parts[0].toIntOrNull() ?: 0
    val minutes = parts[1].toIntOrNull() ?: 0
    val totalMinutes = hours * 60 + minutes
    
    // If all validations are passed, clear the validation message
    _validationMessage.value = null
    
    
    // Create an Exercise object to save, assuming you have a method to actually save this data
    val exercise = Exercise(
      selectedDate.value!!,
      exerciseType.value!!,
      totalMinutes.toUInt(),
      selectedLocation.value?.latitude ?: 22.3375,
      selectedLocation.value?.longitude ?: 114.263,
      exerciseCalories.value!!.toUInt()
    )
    
    currentUser.addEntries(exercise)
  }
}