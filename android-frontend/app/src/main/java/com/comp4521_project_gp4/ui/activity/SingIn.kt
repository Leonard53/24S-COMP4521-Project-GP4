package com.comp4521_project_gp4.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.comp4521_project_gp4.R

class SingIn : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_sing_in)
//    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//      insets
//    }
  }
}