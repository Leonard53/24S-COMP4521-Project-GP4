package com.comp4521_project_gp4.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.Food

class FoodAdapter(private val foodList: List<Food>) :
  RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
  
  class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var foodName: TextView = itemView.findViewById(R.id.food_type)
    var foodCalories: TextView = itemView.findViewById(R.id.food_calories)
    var foodDateTime: TextView = itemView.findViewById(R.id.eating_date_time)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.food_card, parent, false)
    return FoodViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
    holder.foodName.text = foodList[position].foodName
    holder.foodCalories.text = "${foodList[position].foodCalories}"
    holder.foodDateTime.text = foodList[position].date
  }
  
  override fun getItemCount() = foodList.size
}