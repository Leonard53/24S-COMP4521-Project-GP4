package com.comp4521_project_gp4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.model.DashboardModel
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.comp4521_project_gp4.ui.adapters.DashboardAdapter
import com.comp4521_project_gp4.viewmodel.DashboardViewModel

class DashboardViewModel : ViewModel() {
  private val _dashboardItems = MutableLiveData<List<DashboardModel.DashboardItem>>()
  val dashboardItems: LiveData<List<DashboardModel.DashboardItem>> = _dashboardItems
  
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