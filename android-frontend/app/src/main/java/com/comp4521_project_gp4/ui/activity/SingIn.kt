package com.comp4521_project_gp4.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws.User
import kotlinx.coroutines.launch

class SingIn : AppCompatActivity() {
  lateinit var sharedPref: SharedPreferences
  private val startForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
      sharedPref.edit().clear().apply()
      showSignInSignUpBtn()
    }
  
  private fun checkUsernameAndPasswordFilledIn(): Boolean {
    
    val errorText = findViewById<TextView>(R.id.signin_error_text)
    val userNameInput = findViewById<EditText>(R.id.username_text).text
    if (userNameInput.isNullOrEmpty()) {
      "Please enter your username!".also { errorText.text = it }
      errorText.visibility = View.VISIBLE
      showSignInSignUpBtn()
      return false
    }
    val passWordInput = findViewById<EditText>(R.id.password_text).text
    if (passWordInput.isNullOrEmpty()) {
      "Please enter your password!".also { errorText.text = it }
      errorText.visibility = View.VISIBLE
      showSignInSignUpBtn()
      return false
    }
    errorText.visibility = View.GONE
    return true
  }
  
  private fun hideSignInSignUpBtn() {
    val signInBut = findViewById<Button>(R.id.signin_btn)
    val signUpBut = findViewById<Button>(R.id.signup_btn)
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
    signUpBut.visibility = View.GONE
    signUpBut.isEnabled = false
    signInBut.visibility = View.GONE
    signInBut.isEnabled = false
    progressBar.visibility = View.VISIBLE
  }
  
  private fun showSignInSignUpBtn() {
    val signInBut = findViewById<Button>(R.id.signin_btn)
    val signUpBut = findViewById<Button>(R.id.signup_btn)
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
    runOnUiThread {
      signUpBut.isEnabled = true
      signInBut.isEnabled = true
      signUpBut.visibility = View.VISIBLE
      signInBut.visibility = View.VISIBLE
      progressBar.visibility = View.GONE
    }
  }
  
  private fun preSubmitCheck(): User? {
    hideSignInSignUpBtn()
    val filledIn = checkUsernameAndPasswordFilledIn()
    if (filledIn) {
      val userName = findViewById<EditText>(R.id.username_text).text.toString()
      val currentUser = User(userName)
      return currentUser
    }
    return null
  }
  
  private fun signInOrSignUpExceptionHandler(e: Exception) {
    val errorText = findViewById<TextView>(R.id.signin_error_text)
    e.toString().also {
      errorText.text = it
    }
    errorText.visibility = View.VISIBLE
    showSignInSignUpBtn()
  }
  
  private suspend fun signUpUser(): User? {
    val currentUser = preSubmitCheck() ?: return null
    try {
      currentUser.signupUser()
    } catch (e: Exception) {
      signInOrSignUpExceptionHandler(e)
      return null
    }
    sharedPref.edit().putString("username", currentUser.getUsername()).apply()
    loggingIn(mainActivityIntent(currentUser))
    return currentUser
  }
  
  private suspend fun signInUser(): User? {
    val currentUser = preSubmitCheck() ?: return null
    try {
      currentUser.signinUser()
    } catch (e: Exception) {
      signInOrSignUpExceptionHandler(e)
      return null
    }
    sharedPref.edit().putString("username", currentUser.getUsername()).apply()
    loggingIn(mainActivityIntent(currentUser))
    return currentUser
  }
  
  private fun mainActivityIntent(loggedInUser: User): Intent {
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra("user", loggedInUser)
    return intent
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    sharedPref = getSharedPreferences("cachedUser", Context.MODE_PRIVATE)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_sing_in)
    hideSignInSignUpBtn()
    val cachedUsername = sharedPref.getString("username", null)
    if (!cachedUsername.isNullOrEmpty()) {
      lifecycleScope.launch {
        val cachedUser = User(cachedUsername)
        cachedUser.signinUser()
        loggingIn(mainActivityIntent(cachedUser))
      }
    }
    showSignInSignUpBtn()
    val signUpButton = findViewById<Button>(R.id.signup_btn)
    signUpButton.setOnClickListener {
      lifecycleScope.launch {
        signUpUser()
      }
    }
    val signInButton = findViewById<Button>(R.id.signin_btn)
    signInButton.setOnClickListener {
      lifecycleScope.launch { signInUser() }
    }
  }
  
  private fun loggingIn(intent: Intent) {
    startForResult.launch(intent)
  }
}