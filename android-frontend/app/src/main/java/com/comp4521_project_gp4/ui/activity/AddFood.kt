package com.comp4521_project_gp4.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws_lambda.Food
import com.comp4521_project_gp4.backend.aws_lambda.User
import kotlinx.coroutines.launch

class AddFood : AppCompatActivity() {
  
  private lateinit var listView: ListView
  private lateinit var btnAddFood: Button
  private lateinit var sharedPrefs: SharedPreferences
  private lateinit var user: User
  private lateinit var arrAdaptor: ArrayAdapter<String>
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    user = intent.getParcelableExtra<User>("user")!!
    setContentView(R.layout.activity_add_food)
    sharedPrefs = getSharedPreferences("FoodPrefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().clear().apply()
    arrAdaptor = ArrayAdapter(this, android.R.layout.simple_list_item_1)
    lifecycleScope.launch {
      loadFoodRecords()
    }
    listView = findViewById(R.id.lvFoodRecords)
    listView.adapter = arrAdaptor
  }
  
  override fun onResume() {
    super.onResume()
    
    btnAddFood = findViewById(R.id.btnAddFood)
    
    val addfoodreturn_btn = findViewById<TextView>(R.id.addfoodreturn);
    addfoodreturn_btn.setOnClickListener {
      // Create an Intent to start the AddFood Activity
      val intent = Intent(this, MainActivity::class.java)
      finish()
    }
    
    btnAddFood.setOnClickListener {
      val intent = Intent(this, RecordFoodPage::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      
    }
    lifecycleScope.launch {
      loadFoodRecords()
    }
  }
  
  private suspend fun loadFoodRecords() {
    val foodList = mutableListOf<String>()
    //val count = sharedPrefs.getInt("count", 0)
    val allFood = Food.getAllFood(user)
    allFood.forEach { currentFood ->
      val record =
        "Food Name: ${currentFood.foodName}\nCalories: ${currentFood.foodCalories} kcal\nEating Time: ${currentFood.date}"
      foodList.add(record)
    }
    println(foodList.size)
    arrAdaptor.clear()
    arrAdaptor.addAll(foodList)
    arrAdaptor.notifyDataSetChanged()
  }
}