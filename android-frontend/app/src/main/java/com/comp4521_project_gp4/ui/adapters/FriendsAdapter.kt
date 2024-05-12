package com.comp4521_project_gp4.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.yourpackage.databinding.ItemExerciseBinding  // Import your data binding class if using data binding
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.model.FriendsModel

class FriendsAdapter(private var cardItemList: List<FriendsModel.FriendsItem>) :
  RecyclerView.Adapter<FriendsAdapter.CardViewHolder>() {
  class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var friendsName: TextView = itemView.findViewById(R.id.friendCardName)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_card, parent, false)
    return CardViewHolder(itemView)
  }
  
  override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val currentItem = cardItemList[position]
    holder.friendsName.text = currentItem.name
  }
  
  fun updateData(newData: List<FriendsModel.FriendsItem>) {
    cardItemList = newData
    notifyDataSetChanged()
  }
  
  override fun getItemCount() = cardItemList.size
}