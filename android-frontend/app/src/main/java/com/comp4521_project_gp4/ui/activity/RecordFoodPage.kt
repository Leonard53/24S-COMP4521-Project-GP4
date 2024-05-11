package com.comp4521_project_gp4.ui.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws_lambda.Food
import com.comp4521_project_gp4.backend.aws_lambda.User
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordFoodPage : AppCompatActivity() {
  lateinit var currentUser: User
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_record_food_page)
    
    
    var etFoodName = findViewById<EditText>(R.id.etFoodName)
    var btnSaveFood = findViewById<Button>(R.id.btnSaveFood)
    var etCalories = findViewById<EditText>(R.id.etCalories)
    var tvCurrentTime = findViewById<TextView>(R.id.tvCurrentTime)
    
    val user = intent.getParcelableExtra<User>("user")  //get username
    currentUser = user!!
    
    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    tvCurrentTime.text = "Eating Time: $currentTime"
    
    
    btnSaveFood.setOnClickListener {
      
      val foodName = etFoodName.text.toString()
      val calories = etCalories.text.toString()
      
      val food = Food(currentTime, foodName, calories.toUInt()) //create object
      lifecycleScope.launch { currentUser.addEntries(food) }         //upload data
      //saveFoodRecord(foodName, calories, currentTime)
      finish()
    }
  }
}