package com.comp4521_project_gp4.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.model.ExerciseModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ExerciseAdapter(private var cardItemList: List<ExerciseModel.ExerciseItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var expandedPosition = RecyclerView.NO_POSITION
  private val mapViewList = mutableListOf<MapView>()
  
  companion object {
    const val VIEW_TYPE_COLLAPSED = 0
    const val VIEW_TYPE_EXPANDED = 1
  }
  
  override fun getItemViewType(position: Int): Int {
    return if (position == expandedPosition) VIEW_TYPE_EXPANDED else VIEW_TYPE_COLLAPSED
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return if (viewType == VIEW_TYPE_EXPANDED) {
      val itemView = inflater.inflate(R.layout.exercise_card_expanded, parent, false)
      ExpandedViewHolder(itemView, mapViewList)
    } else {
      val itemView = inflater.inflate(R.layout.exercise_card, parent, false)
      CollapsedViewHolder(itemView)
    }
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = cardItemList[position]
    
    when (holder) {
      is CollapsedViewHolder -> {
        holder.exerciseType.text = currentItem.exerciseType
        holder.exerciseDateTime.text = currentItem.exerciseDateTime
        holder.exerciseCalories.text = currentItem.exerciseCalories.toString()      }
      is ExpandedViewHolder -> {
        holder.exerciseType.text = currentItem.exerciseType
        holder.exerciseDateTime.text = currentItem.exerciseDateTime
        holder.exerciseCalories.text = currentItem.exerciseCalories.toString()
        holder.exerciseDuration.text = currentItem.exerciseDuration.toString()
        holder.exerciseLat = currentItem.latitude!! // Assuming these fields exist
        holder.exerciseLng = currentItem.longitude!!
        // MapView is already being handled in the ViewHolder's init block
      }
    }
    
    // Toggle expanded/collapsed state
    holder.itemView.setOnClickListener {
      val newExpandedPosition = if (expandedPosition == position) RecyclerView.NO_POSITION else position
      notifyItemChanged(expandedPosition)
      notifyItemChanged(position)
      expandedPosition = newExpandedPosition
    }
  }
  override fun getItemCount() = cardItemList.size
  
  class CollapsedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var exerciseType: TextView = itemView.findViewById(R.id.exercise_type)
    var exerciseDateTime: TextView = itemView.findViewById(R.id.exercise_date_time)
    var exerciseCalories: TextView = itemView.findViewById(R.id.exercise_calories)
  }
  
  class ExpandedViewHolder(itemView: View, mapViewList: MutableList<MapView>) : RecyclerView.ViewHolder(itemView), OnMapReadyCallback {
    var exerciseType: TextView = itemView.findViewById(R.id.exercise_type_expanded)
    var exerciseDateTime: TextView = itemView.findViewById<TextView?>(R.id.exercise_date_time_expanded)
    var exerciseCalories: TextView = itemView.findViewById(R.id.exercise_calories_expanded)
    var exerciseDuration: TextView = itemView.findViewById(R.id.exercise_duration_expanded)
    var mapView: MapView = itemView.findViewById(R.id.mapView)
    var exerciseLat: Double = 0.0
    var exerciseLng: Double = 0.0
    
    init {
      mapView.onCreate(null)
      mapView.getMapAsync(this)
      mapViewList.add(mapView) // Track for lifecycle management
    }
    
    override fun onMapReady(p0: GoogleMap) {
      p0?.apply {
        // Now you can use the latitude and longitude from the item
        val location = LatLng(exerciseLat, exerciseLng)
        
        moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        addMarker(MarkerOptions().position(location).title("Marker"))
        
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true
      }
    }
  }
  
  fun updateData(newData: List<ExerciseModel.ExerciseItem>) {
    cardItemList = newData
    notifyDataSetChanged()
  }
  
  // Lifecycle methods for MapView
  fun mapViewOnResume() {
    mapViewList.forEach { it.onResume() }
  }
  
  fun mapViewOnPause() {
    mapViewList.forEach { it.onPause() }
  }
  
  fun mapViewOnDestroy() {
    mapViewList.forEach { it.onDestroy() }
  }
  
  fun mapViewOnSaveInstanceState(outState: Bundle) {
    mapViewList.forEach { it.onSaveInstanceState(outState) }
  }
  
  fun mapViewOnLowMemory() {
    mapViewList.forEach { it.onLowMemory() }
  }
}