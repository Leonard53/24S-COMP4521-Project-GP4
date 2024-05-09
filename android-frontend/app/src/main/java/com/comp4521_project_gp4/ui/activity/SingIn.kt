package com.comp4521_project_gp4.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.backend.aws_lambda.User
import kotlinx.coroutines.launch

class SingIn : AppCompatActivity() {
  private fun checkUsernameAndPasswordFilledIn(): Boolean {
    val errorText = findViewById<TextView>(R.id.signin_error_text)
    val userNameInput = findViewById<EditText>(R.id.username_text).text
    if (userNameInput == null) {
      "Please enter your username!".also { errorText.text = it }
      errorText.visibility = View.VISIBLE
      return false
    }
    val passWordInput = findViewById<EditText>(R.id.password_text).text
    if (passWordInput == null) {
      "Please enter your password!".also { errorText.text = it }
      errorText.visibility = View.VISIBLE
      return false
    }
    errorText.visibility = View.GONE
    return true
  }
  
  private suspend fun signUpUser(): User? {
    val filledIn = checkUsernameAndPasswordFilledIn()
    if (filledIn) {
      val userName = findViewById<EditText>(R.id.username_text).text.toString()
      val currentUser = User(userName)
      try {
        currentUser.signupUser()
      } catch (e: Exception) {
        val errorText = findViewById<TextView>(R.id.signin_error_text)
        e.toString().also {
          errorText.text = it
        }
        errorText.visibility = View.VISIBLE
      }
      return currentUser
    }
    return null
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_sing_in)
    val signUpButton = findViewById<Button>(R.id.signup_btn)
    signUpButton.setOnClickListener {
      lifecycleScope.launch {
        signUpUser()
      }
    }
  }
}