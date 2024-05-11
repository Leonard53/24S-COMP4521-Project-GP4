package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.model.FriendsModel

class FriendViewModel : ViewModel() {
  private val _friends = MutableLiveData<List<FriendsModel.FriendsItem>>()
  val friends: LiveData<List<FriendsModel.FriendsItem>> = _friends

  init {
    loadFriends()
  }

  private fun loadFriends() {
    // Simulate loading data
    val friendList = listOf(
      FriendsModel.FriendsItem("Karen"),
      FriendsModel.FriendsItem("Jacky")
    )
    _friends.value = friendList
  }
}