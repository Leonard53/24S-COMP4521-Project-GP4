package com.comp4521_project_gp4.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.comp4521_project_gp4.R

class AddFood : AppCompatActivity() {
  
  private var foodList = ArrayList<String>()
  private lateinit var listView: ListView
  private lateinit var btnAddFood: Button
  private lateinit var sharedPrefs: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_food)
    sharedPrefs = getSharedPreferences("FoodPrefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().clear().apply()
  }
  
  override fun onResume() {
    super.onResume()
    
    listView = findViewById(R.id.lvFoodRecords)
    btnAddFood = findViewById(R.id.btnAddFood)
    
    btnAddFood.setOnClickListener {
      val intent = Intent(this, RecordFoodPage::class.java).also {
        startActivity(it)
      }
    }
    
    loadFoodRecords()
    listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodList)
    
  }
  
  
  private fun loadFoodRecords() {
    val count = sharedPrefs.getInt("count", 0)
    foodList.clear()// 清除舊數據
    for (i in 1..count) {
      sharedPrefs.getString("foodRecord_$i", null)?.let {
        foodList.add(it)
      }
    }
    
  }
  
}