package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.model.FriendsModel
import kotlinx.coroutines.launch

class FriendViewModel : ViewModel() {
  private val _friends = MutableLiveData<List<FriendsModel.FriendsItem>>()
  val friends: LiveData<List<FriendsModel.FriendsItem>> = _friends
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
    if (!currentUser.isInitialized || currentUser.value == null) {
      friendList.add(FriendsModel.FriendsItem("Karen"))
      friendList.add(FriendsModel.FriendsItem("Jacky"))
    } else {
      (currentUser.value?.getUserFriend() ?: emptyList()).forEach { currentFriend ->
        friendList.add(
          FriendsModel.FriendsItem(currentFriend)
        )
      }
    }
    _friends.value = friendList
  }
}