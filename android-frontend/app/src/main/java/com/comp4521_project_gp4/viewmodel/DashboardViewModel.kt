package com.comp4521_project_gp4.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.model.DashboardModel
import androidx.lifecycle.viewModelScope
import com.comp4521_project_gp4.backend.aws.Exercise
import com.comp4521_project_gp4.backend.aws.Food
import com.comp4521_project_gp4.backend.aws.User
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
  private val _dashboardItems = MutableLiveData<List<DashboardModel.DashboardItem>>()
  private val _dashboardVisibility = MutableLiveData<DashboardModel.DashboardChartItem>(
    DashboardModel.DashboardChartItem(View.GONE, mutableMapOf())
  )
  val dashboardItems: LiveData<List<DashboardModel.DashboardItem>> = _dashboardItems
  val dashboardVisibility: LiveData<DashboardModel.DashboardChartItem> = _dashboardVisibility
  

  private val currentUser = MutableLiveData<User>()
  fun setCurrentUser(user: User) {
    currentUser.value = user
    viewModelScope.launch {
      loadData()
    }
  }
  init {
    viewModelScope.launch {
      loadData()
    }
  }
 
  suspend fun calculateUserScore(user: User): Int {
    user.updateFoodAndExerciseCache()
    val exercises = user.getCurrentUserExerciseCache()
//    val foods = user.getCurrentUserFoodCache()
    val thisWeekExercises = exercises.filter { exercise: Exercise -> MainViewModel.isExerciseInCurrentWeek(exercise)}
    val combinedCaloriesInExercise = thisWeekExercises.fold(0)
    {acc: Int , exercise -> acc + exercise.calories.toInt()}
//    val thisWeekFood = foods.filter { food: Food -> MainViewModel.isFoodInCurrentWeek(food)}
//    val combinedCaloriesIntake = thisWeekFood.fold(0)
//    {acc: Int, food: Food -> acc + food.foodCalories.toInt()}
    val finalScore = combinedCaloriesInExercise
    return finalScore
  }
  
  suspend fun loadData() {
    // Simulate loading data
    val leaderboardList = mutableListOf<DashboardModel.DashboardItem>()
    val mapScore = mutableMapOf<String, Int>()
    if (currentUser.isInitialized && currentUser.value != null) {
      val userScore = calculateUserScore(currentUser.value!!)
      val displayUserScore = DashboardModel.DashboardItem(userScore, "You")
      leaderboardList.add(displayUserScore)
      mapScore["You"] = userScore
      val currentUserFriendList = currentUser.value?.getUserFriend() ?: emptyList()
      currentUserFriendList.forEach { currentFriend ->
        val currentFriendScore = calculateUserScore(User(currentFriend))
        val currentAdapterElement = DashboardModel.DashboardItem(currentFriendScore, currentFriend)
        leaderboardList.add(currentAdapterElement)
        mapScore[currentFriend] = currentFriendScore
      }
      _dashboardItems.value = leaderboardList
      val newDashboardVisibility = DashboardModel.DashboardChartItem(View.VISIBLE, mapScore)
      _dashboardVisibility.value = newDashboardVisibility
    }
  }
}