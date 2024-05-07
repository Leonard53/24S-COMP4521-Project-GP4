package com.comp4521_project_gp4.backend.aws_lambda

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeAction
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.AttributeValueUpdate
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import java.lang.Exception

class User(private val userName: String) {
  private val currentUserKeyInDB = mutableMapOf("username" to AttributeValue.S(this.userName))
  private var currentUserExerciseCache = mutableListOf(mutableMapOf<String, AttributeValue>())
  private val ddb = DynamoDbClient { region = "ap-east-1" }
  
  init {
    suspend {
      val existingExerciseLog = getAllExercises()
      if (existingExerciseLog.isEmpty()) {
        /* User does not have any exercise log. Creating an empty list of exercise log */
        val updateItemRequest = UpdateItemRequest {
          tableName = EXECERCISE_TABLE_NAME
          key = currentUserKeyInDB
          attributeUpdates = mutableMapOf(
            "exercise_log" to AttributeValueUpdate {
              value = AttributeValue.L(emptyList())
              action = AttributeAction.Put
            }
          )
        }
        ddb.updateItem(updateItemRequest)
      }
      currentUserExerciseCache = existingExerciseLog
    }
  }
  
  suspend fun addExercise(newExercise: Exercise) {
    val newItem = mutableMapOf<String, AttributeValue>()
    newItem["date"] = AttributeValue.S(newExercise.date)
    newItem["length"] = AttributeValue.N(newExercise.exerciseLengthInMins.toString())
    newItem["calories"] = AttributeValue.N(newExercise.calories.toString())
    val updateItem = mutableMapOf<String, AttributeValue>()
    updateItem[":newItem"] = AttributeValue.M(newItem)
    val request = UpdateItemRequest {
      tableName = EXECERCISE_TABLE_NAME
      key = currentUserKeyInDB
      updateExpression = "SET exercise_log = list_append(exercise_log, :newItem)"
      expressionAttributeValues = updateItem
    }
    ddb.updateItem(request)
    currentUserExerciseCache.add(updateItem)
  }
  
  suspend fun getAllExercises(): MutableList<MutableMap<String, AttributeValue>> {
    val ddb = DynamoDbClient { region = "ap-east-1" }
    val getItemRequest = GetItemRequest {
      tableName = EXECERCISE_TABLE_NAME
      key = currentUserKeyInDB
    }
    val existingExerciseLog: MutableList<MutableMap<String, AttributeValue>> = mutableListOf()
    try {
      val res = ddb.getItem(getItemRequest)
      val logObtained = res.item?.get("exercise_log")?.asL()
      logObtained?.forEach { currentList ->
        val returnMap = currentList.asM()
        existingExerciseLog.add(returnMap as MutableMap<String, AttributeValue>)
      }
    } catch (_: Exception) {
    }
    return existingExerciseLog
  }
}