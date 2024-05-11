package com.comp4521_project_gp4.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.yourpackage.databinding.ItemExerciseBinding  // Import your data binding class if using data binding
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.model.ExerciseModel

class ExerciseAdapter(private var cardItemList: List<ExerciseModel.ExerciseItem>) : RecyclerView.Adapter<ExerciseAdapter.CardViewHolder>() {
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exerciseType: TextView = itemView.findViewById(R.id.exercise_type)
        var exerciseDateTime: TextView = itemView.findViewById(R.id.exercise_date_time)
        var exerciseCalories: TextView = itemView.findViewById(R.id.exercise_calories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_card, parent, false)
        return CardViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardItemList[position]
        holder.exerciseType.text = currentItem.exerciseType
        holder.exerciseDateTime.text = currentItem.exerciseDateTime
        holder.exerciseCalories.text = currentItem.exerciseCalories.toString()
    }

    fun updateData(newData: List<ExerciseModel.ExerciseItem>) {
        cardItemList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = cardItemList.size

}