package com.comp4521_project_gp4.backend.aws_lambda

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeAction
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.AttributeValueUpdate
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import aws.smithy.kotlin.runtime.collections.push
import aws.smithy.kotlin.runtime.util.type
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlin.Exception

const val USERDB_NAME = "COMP4521-user-db"
val ddb = DynamoDbClient {
  region = "ap-east-1"
  credentialsProvider = StaticCredentialsProvider {
    accessKeyId = "AKIA3FLDYAGWZKNIXX57"
    secretAccessKey = "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"
  }
}

@Parcelize
class User(
  private val userName: String,
  private val currentUserExerciseCache: MutableList<Exercise> = mutableListOf(),
  private val currentUserFoodCache: MutableList<Food> = mutableListOf()
) : Parcelable {
  
  @IgnoredOnParcel
  private val currentUserKeyInDB: MutableMap<String, AttributeValue.S> = mutableMapOf(
    "username" to AttributeValue.S(userName)
  )
  
  fun getCurrentUserKeyInDB(): MutableMap<String, AttributeValue.S> {
    return currentUserKeyInDB
  }
  
  fun addCurrentUserExerciseCache(exercise: Exercise) {
    currentUserExerciseCache.add(exercise)
  }
  
  fun getCurrentUserExerciseCache(): MutableList<Exercise> {
    return currentUserExerciseCache
  }
  
  fun addCurrentUserFoodCache(food: Food) {
    currentUserFoodCache.add(food)
  }
  
  
  fun getCurrentUserFoodCache(): MutableList<Food> {
    return currentUserFoodCache
  }
  
  suspend fun getProfilePicUrl(): String {
    val req = GetItemRequest {
      tableName = USERDB_NAME
      key = currentUserKeyInDB
    }
    return ddb.getItem(req).item?.get("profile_pic")?.asS() ?: ""
  }
  
  suspend fun setProfilePicUrl(newProfilePicUrl: String) {
    val updateValues = mutableMapOf<String, AttributeValueUpdate>()
    updateValues["profile_pic"] = AttributeValueUpdate {
      value = AttributeValue.S(newProfilePicUrl)
      action = AttributeAction.Put
    }
    val req = UpdateItemRequest {
      tableName = USERDB_NAME
      key = currentUserKeyInDB
      attributeUpdates = updateValues
    }
    ddb.updateItem(req)
  }
  
  suspend fun signupUser() {
    val req = GetItemRequest {
      tableName = USERDB_NAME
      key = currentUserKeyInDB
    }
    try {
      val existingUser = ddb.getItem(req)
      if (existingUser.item != null) throw Exception()
      val emptyItem = mutableMapOf<String, AttributeValue>()
      emptyItem["username"] = AttributeValue.S(this.userName)
      emptyItem["exercise_log"] = AttributeValue.L(emptyList<AttributeValue.M>())
      emptyItem["food_log"] = AttributeValue.L(emptyList<AttributeValue.M>())
      emptyItem["profile_pic"] = AttributeValue.S("")
      val signupRequest = PutItemRequest {
        tableName = USERDB_NAME
        item = emptyItem
      }
      ddb.putItem(signupRequest)
    } catch (_: Exception) {
      throw Exception("User already exist")
    }
  }
  
  suspend fun signinUser() {
    val req = GetItemRequest {
      tableName = USERDB_NAME
      key = currentUserKeyInDB
    }
    try {
      val existingUser = ddb.getItem(req)
      if (existingUser.item.isNullOrEmpty()) throw Exception()
      val rawExerciseLog = existingUser.item!!["exercise_log"]?.asL() ?: emptyList()
      currentUserExerciseCache.clear()
      Exercise.getAllExercises(rawExerciseLog)
        .forEach { currentExercise -> currentUserExerciseCache.add(currentExercise) }
      val rawFoodLog = existingUser.item!!["food_log"]?.asL() ?: emptyList()
      currentUserFoodCache.clear()
      Food.getAllFood(rawFoodLog).forEach { currentFood ->
        currentUserFoodCache.add(currentFood)
      }
    } catch (_: Exception) {
      throw Exception("User does not exist")
    }
  }
  
  suspend fun addEntries(exerciseOrFood: ExerciseOrFood) {
    try {
      val updateReq = exerciseOrFood.updateRequest(this)
      ddb.updateItem(updateReq)
      if (exerciseOrFood is Exercise) {
        addCurrentUserExerciseCache(exerciseOrFood)
      } else if (exerciseOrFood is Food) {
        addCurrentUserFoodCache(exerciseOrFood)
      }
    } catch (e: Exception) {
      throw e
    }
  }
  
}