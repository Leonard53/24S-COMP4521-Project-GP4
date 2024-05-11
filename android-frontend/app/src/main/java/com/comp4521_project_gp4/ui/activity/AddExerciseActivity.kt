package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.User
import com.comp4521_project_gp4.ui.viewmodel.AddExerciseViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity(), OnMapReadyCallback {
  private lateinit var mMap: GoogleMap
  private val addExerciseViewModel: AddExerciseViewModel by viewModels()
  private val MY_LOCATION_REQUEST_CODE = 101
  private lateinit var currentUser: User
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_exercise)
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
    
    currentUser = intent.getParcelableExtra<User>("user")!!
    currentUser.let {
      addExerciseViewModel.setUser(it)
    }
    
    val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
    mapFragment.getMapAsync(this)
    val dateInput = findViewById<TextInputEditText>(R.id.exerciseDateEditText)
    val timeInput = findViewById<TextInputEditText>(R.id.exerciseDurationEditText)
    val exerciseTypeInput = findViewById<TextInputEditText>(R.id.activityTypeEditText)
    val caloriesInput = findViewById<TextInputEditText>(R.id.caloriesEditText)
    val saveBtn: MaterialButton = findViewById(R.id.saveExerciseButton)
    
    saveBtn.setOnClickListener {
      if (addExerciseViewModel.validateForm()) {
        lifecycleScope.launch {
          addExerciseViewModel.saveData()
          finish()
        }
      }
    }
    
    // Add text change listener to exerciseTypeInput
    exerciseTypeInput.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {
        addExerciseViewModel.setExerciseType(s.toString().trim())
      }
    })
    
    // Add text change listener to caloriesInput
    caloriesInput.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {
        addExerciseViewModel.setCalories(s.toString().trim())
      }
    })
    
    // Observe the validation message LiveData from the ViewModel
    addExerciseViewModel.validationMessage.observe(this) { message ->
      message?.let {
        // Display the validation message to the user, for example using a Toast
        Snackbar.make(findViewById(R.id.addActivityContainer), message.toString(), Snackbar.LENGTH_LONG).show()

        // Reset the validation message in ViewModel after showing it
        addExerciseViewModel.clearValidationMessage()
      }
    }
    
    addExerciseViewModel.selectedDate.observe(this) { date ->
      dateInput.setText(date)
    }
    
    addExerciseViewModel.selectedTime.observe(this) { time ->
      timeInput.setText(time)
    }
    
    dateInput.setOnClickListener {
      showDatePicker()
    }
    
    timeInput.setOnClickListener {
      showTimePicker()
    }
    
    checkLocationPermission()
  }
  
  private fun checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
        MY_LOCATION_REQUEST_CODE
      )
    }
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == MY_LOCATION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
      ) {
        mMap.isMyLocationEnabled = true
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        val location = provider?.let { locationManager.getLastKnownLocation(it) }
        location?.let {
          val userLatLng = LatLng(it.latitude, it.longitude)
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
        }
      }
    } else {
      // Handle case where permission was denied
    }
  }
  
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED
    ) {
      mMap.isMyLocationEnabled = true
      val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
      val provider = locationManager.getBestProvider(Criteria(), true)
      val location = provider?.let { locationManager.getLastKnownLocation(it) }
      location?.let {
        val userLatLng = LatLng(it.latitude, it.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
        addExerciseViewModel.setLocation(userLatLng) // Save location to ViewModel
      }
    }
  }
  
  private fun showDatePicker() {
    val datePicker = MaterialDatePicker.Builder.datePicker()
      .setTitleText("Select date")
      .build()
    datePicker.addOnPositiveButtonClickListener { date ->
      addExerciseViewModel.setDate(datePicker.headerText)
    }
    datePicker.show(supportFragmentManager, "datePicker")
  }
  
  private fun showTimePicker() {
    val timePicker = MaterialTimePicker.Builder()
      .setTimeFormat(TimeFormat.CLOCK_24H)
      .setTitleText("Select Time")
      .build()
    timePicker.addOnPositiveButtonClickListener {
      val timeString = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
      addExerciseViewModel.setTime(timeString)
    }
    timePicker.show(supportFragmentManager, "timePicker")
  }
}