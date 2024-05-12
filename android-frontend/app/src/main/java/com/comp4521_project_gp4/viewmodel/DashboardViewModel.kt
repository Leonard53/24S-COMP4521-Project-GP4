package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.model.DashboardModel
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.model.FriendsModel
import com.comp4521_project_gp4.ui.adapters.DashboardAdapter
import com.comp4521_project_gp4.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
  private val _dashboardItems = MutableLiveData<List<DashboardModel.DashboardItem>>()
  val dashboardItems: LiveData<List<DashboardModel.DashboardItem>> = _dashboardItems
  

  private val currentUser = MutableLiveData<User>()
  fun setCurrentUser(user: User) {
    currentUser.value = user
    viewModelScope.launch {
      loadFriends()
    }
  }
  
  init {
    viewModelScope.launch {
      loadFriends()
    }
  }
  
  suspend fun loadFriends() {
    // Simulate loading data
    val friendList = mutableListOf<FriendsModel.FriendsItem>()
    val leaderboardlist = mutableListOf<DashboardModel.DashboardItem>()
    if (!currentUser.isInitialized || currentUser.value == null) {
      friendList.add(FriendsModel.FriendsItem("Karen"))
      friendList.add(FriendsModel.FriendsItem("Jacky"))
    } else {
      val currentfriend = currentUser.value!!.getUserFriend()
      (currentUser.value?.getUserFriend() ?: emptyList()).forEach { currentFriend ->
        friendList.add(
          FriendsModel.FriendsItem(currentFriend)
        )
      }
    }
    _friends.value = friendList
  }
  
  
  
  init {
    loadDashboardItems()
  }
  
  private fun loadDashboardItems() {
    // Simulate loading data
    val dashboardList = listOf(
      DashboardModel.DashboardItem(300, "Alice"),
      DashboardModel.DashboardItem(450, "Bob"),
      DashboardModel.DashboardItem(500, "Charlie")
      // Add more items as needed
    )
    _dashboardItems.value = dashboardList
  }
}