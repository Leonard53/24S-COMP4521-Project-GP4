package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.Food
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.ui.adapters.FoodAdapter
import kotlinx.coroutines.launch

class AddFood : AppCompatActivity() {
  
  private lateinit var recyclerView: RecyclerView
  private lateinit var btnAddFood: Button
  private lateinit var user: User
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_food)
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
    
    user = intent.getParcelableExtra<User>("user")!!
    btnAddFood = findViewById(R.id.btnAddFood)
    recyclerView = findViewById(R.id.rvFoodRecords)
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    lifecycleScope.launch {
      loadFoodRecords()
    }
  }
  
  private suspend fun loadFoodRecords() {
    val allFood = Food.getAllFood(user)
    recyclerView.adapter = FoodAdapter(allFood)
  }
  
  override fun onResume() {
    super.onResume()
    btnAddFood.setOnClickListener {
      val intent = Intent(this, RecordFoodPage::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
    }
    lifecycleScope.launch {
      loadFoodRecords()
    }
  }
}