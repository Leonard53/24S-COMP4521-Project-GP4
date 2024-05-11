package com.comp4521_project_gp4.backend.aws_lambda

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.smithy.kotlin.runtime.util.type
import kotlin.Exception

const val USERDB_NAME = "COMP4521-user-db"

class User(private val userName: String) {
  private val currentUserKeyInDB = mutableMapOf("username" to AttributeValue.S(this.userName))
  fun getCurrentUserKeyInDB(): MutableMap<String, AttributeValue.S> {
    return currentUserKeyInDB
  }
  
  private val currentUserExerciseCache = mutableListOf<Exercise>()
  fun addCurrentUserExerciseCache(exercise: Exercise) {
    currentUserExerciseCache.add(exercise)
  }
  
  fun getCurrentUserExerciseCache(): MutableList<Exercise> {
    return currentUserExerciseCache
  }
  
  private val currentUserFoodCache = mutableListOf<Food>()
  fun addCurrentUserFoodCache(food: Food) {
    currentUserFoodCache.add(food)
  }
  
  
  fun getCurrentUserFoodCache(): MutableList<Food> {
    return currentUserFoodCache
  }
  
  private val ddb = DynamoDbClient {
    region = "ap-east-1"
    credentialsProvider = StaticCredentialsProvider {
      accessKeyId = "AKIA3FLDYAGWZKNIXX57"
      secretAccessKey = "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"
    }
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
      val signupRequest = PutItemRequest {
        tableName = USERDB_NAME
        item = emptyItem
      }
      ddb.putItem(signupRequest)
    } catch (_: Exception) {
      throw Exception("User already exist")
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