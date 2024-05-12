package com.comp4521_project_gp4.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.model.DashboardModel

class DashboardAdapter(private var dashboardItemList: List<DashboardModel.DashboardItem>) :
  RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {
  
  class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var friendName: TextView = itemView.findViewById(R.id.dashboard_friend_name)
    var burnedCalories: TextView = itemView.findViewById(R.id.dashboard_burned_calories)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
    val itemView =
      LayoutInflater.from(parent.context).inflate(R.layout.dashboard_card, parent, false)
    return DashboardViewHolder(itemView)
  }
  
  override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
    val currentItem = dashboardItemList[position]
    holder.friendName.text = currentItem.friendName
    holder.burnedCalories.text = currentItem.burnedCalories.toString()
  }
  
  fun updateData(newData: List<DashboardModel.DashboardItem>) {
    dashboardItemList = newData
    notifyDataSetChanged()
  }
  
  override fun getItemCount() = dashboardItemList.size
}