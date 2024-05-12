package com.comp4521_project_gp4.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp4521_project_gp4.backend.aws.Exercise
import com.comp4521_project_gp4.backend.aws.Food
import com.comp4521_project_gp4.backend.aws.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.function.Consumer


class MainViewModel : ViewModel() {
  private lateinit var currentUser: User
  
  private val _caloriesBurned = MutableLiveData<Int>()
  val caloriesBurned: LiveData<Int> get() = _caloriesBurned
  
  private val _caloriesIntake = MutableLiveData<Int>()
  val caloriesIntake: LiveData<Int> get() = _caloriesIntake
  
  private val _exerciseTime = MutableLiveData<Int>()
  val exerciseTime: LiveData<Int> get() = _exerciseTime
  private val today = LocalDate.now()
  private val weekFields = WeekFields.of(Locale.getDefault())
  private val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
  
  fun setUser(user: User) {
    currentUser = user
  }
  
  private fun updateCaloriesBurned(calories: Int) {
    _caloriesBurned.postValue(calories)
  }
  
  private fun updateCaloriesIntake(calories: Int) {
    _caloriesIntake.postValue(calories)
  }
  
  private fun updateExerciseTime(time: Int) {
    _exerciseTime.postValue(time)
  }
  
  fun mainScreenOnLoad() {
    var weeklyCaloriesBurned = 0
    var weeklyExerciseTime = 0
    var weeklyCaloriesIntake = 0
    viewModelScope.launch {
      async { currentUser.updateFoodAndExerciseCache() }.await()
      
      currentUser.getCurrentUserExerciseCache().forEach(Consumer { exercise: Exercise ->
        if (isExerciseInCurrentWeek(exercise)) {
          weeklyCaloriesBurned += exercise.calories.toInt()
          weeklyExerciseTime += exercise.exerciseLengthInMins.toInt()
        }
      })
      
      currentUser.getCurrentUserFoodCache().forEach(Consumer { food: Food ->
        // Check if the week of the year matches the current week
        if (isFoodInCurrentWeek(food)) {
          weeklyCaloriesIntake += food.foodCalories.toInt()
        }
      })
      updateCaloriesBurned(weeklyCaloriesBurned)
      updateExerciseTime(weeklyExerciseTime)
      updateCaloriesIntake(weeklyCaloriesIntake)
    }
  }
  
  companion object {
    private val today = LocalDate.now()
    
    @SuppressLint("ConstantLocale")
    private val weekFields = WeekFields.of(Locale.getDefault())
    private val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
    fun isExerciseInCurrentWeek(exercise: Exercise): Boolean {
      // Define the formatter for the date pattern "May 11, 2024"
      val formatter =
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
      
      // Parse the date using the formatter
      val exerciseDate = LocalDate.parse(exercise.date, formatter)
      
      // Check if the week of the year matches the current week
      return exerciseDate[weekFields.weekOfWeekBasedYear()] == currentWeek
    }
    
    fun isFoodInCurrentWeek(food: Food): Boolean {
      // Creating a formatter that can handle date and time
      val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      
      // Parse the LocalDateTime from the string
      val foodDateTime = LocalDateTime.parse(food.date, formatter)
      
      // Convert LocalDateTime to LocalDate
      val foodDate = foodDateTime.toLocalDate()
      
      // Check if the week of the year matches the current week
      return foodDate[weekFields.weekOfWeekBasedYear()] == currentWeek
    }
  }
}