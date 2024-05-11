package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.comp4521_project_gp4.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class AddExerciseActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_exercise)
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
    
    val dateInput = findViewById<TextInputEditText>(R.id.exerciseDateEditText)
    val durationInput = findViewById<TextInputEditText>(R.id.exerciseDurationEditText)
    
    // Set up click listeners for dateTimeEditText
    dateInput.setOnClickListener {
      showDatePicker()
    }
    
    durationInput.setOnClickListener {
      showTimePicker()
    }
    
  }
  
  private fun showDatePicker() {
    val dateInput = findViewById<TextInputEditText>(R.id.exerciseDateEditText)
    
    val datePicker = MaterialDatePicker.Builder.datePicker()
      .setTitleText("Select date")
      .build()
    datePicker.addOnPositiveButtonClickListener { date ->
      // Format the date and set it to the EditText
      dateInput.setText(datePicker.headerText)
    }
    datePicker.show(supportFragmentManager, datePicker.toString())
  }
  
  private fun showTimePicker() {
    val durationInput = findViewById<TextInputEditText>(R.id.exerciseDurationEditText)
    
    val timePicker = MaterialTimePicker.Builder()
      .setTitleText("Select Time")
      .setTimeFormat(TimeFormat.CLOCK_24H)
      .build()
    timePicker.addOnPositiveButtonClickListener {
      // You could format the time and append it to the date
      val pickedHour = timePicker.hour
      val pickedMinute = timePicker.minute
      val timeString = String.format("%02d:%02d", pickedHour, pickedMinute)
      durationInput.setText("$timeString")
    }
    timePicker.show(supportFragmentManager, timePicker.toString())
  }
}