package com.comp4521_project_gp4.ui.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.comp4521_project_gp4.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordFoodPage : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_record_food_page)
    
    
    var etFoodName = findViewById<EditText>(R.id.etFoodName)
    var btnSaveFood = findViewById<Button>(R.id.btnSaveFood)
    var etCalories = findViewById<EditText>(R.id.etCalories)
    var tvCurrentTime = findViewById<TextView>(R.id.tvCurrentTime)
    
    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    tvCurrentTime.text = "Eating Time: $currentTime"
    
    
    btnSaveFood.setOnClickListener {
      val foodName = etFoodName.text.toString()
      val calories = etCalories.text.toString()
      saveFoodRecord(foodName, calories, currentTime)
      finish()
    }
  }
  
  private fun saveFoodRecord(foodName: String, calories: String, eatTime: String) {
    val sharedPrefs = getSharedPreferences("FoodPrefs", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    val count = sharedPrefs.getInt("count", 0) + 1
    val record = "Food Name: $foodName\nCalories: $calories kcal\nEating Time: $eatTime"
    editor.apply {
      putString("foodRecord_$count", record)
      putInt("count", count)
      apply()
    }
  }
  
}