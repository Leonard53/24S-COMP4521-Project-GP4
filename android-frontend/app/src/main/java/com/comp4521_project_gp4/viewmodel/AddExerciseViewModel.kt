package com.comp4521_project_gp4.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comp4521_project_gp4.R
import com.google.android.gms.maps.model.LatLng

class AddExerciseViewModel(application: Application) : AndroidViewModel(application) {
  private val _selectedLocation = MutableLiveData<LatLng>()
  val selectedLocation: LiveData<LatLng> = _selectedLocation
  
  private val _selectedDate = MutableLiveData<String>()
  val selectedDate: LiveData<String> = _selectedDate
  
  private val _selectedTime = MutableLiveData<String>()
  val selectedTime: LiveData<String> = _selectedTime
  
  fun setLocation(location: LatLng) {
    _selectedLocation.value = location
  }
  
  fun setDate(date: String) {
    _selectedDate.value = date
  }
  
  fun setTime(time: String) {
    _selectedTime.value = time
  }
}