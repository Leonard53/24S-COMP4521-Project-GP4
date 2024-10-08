package com.comp4521_project_gp4.model

class DashboardModel {
  data class DashboardItem(
    val burnedCalories: Int,
    val friendName: String,
  )
  
  data class DashboardChartItem(
    val visibility: Int,
    val userScore: MutableMap<String, Int>
  )
}
