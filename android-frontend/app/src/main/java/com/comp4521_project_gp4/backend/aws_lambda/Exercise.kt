package com.comp4521_project_gp4.backend.aws_lambda

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import aws.smithy.kotlin.runtime.util.toNumber

data class Exercise(
  val date: String,
  val exerciseName: String,
  val exerciseLengthInMins: UInt,
  val calories: UInt
) : ExerciseOrFood() {
  override fun convertToMap(): MutableMap<String, AttributeValue> {
    val returnMap = mutableMapOf<String, AttributeValue>()
    returnMap["date"] = AttributeValue.S(date)
    returnMap["name"] = AttributeValue.S(exerciseName)
    returnMap["length"] = AttributeValue.N(exerciseLengthInMins.toString())
    returnMap["calories"] = AttributeValue.N(calories.toString())
    return returnMap
  }
  
  override suspend fun updateRequest(user: User): UpdateItemRequest {
    val updateItem = mutableMapOf<String, AttributeValue>()
    updateItem[":newItem"] = AttributeValue.M(convertToMap())
    val req = UpdateItemRequest {
      tableName = USERDB_NAME
      key = user.getCurrentUserKeyInDB()
      updateExpression = "SET food_log = list_append(food_log, :newItem)"
      expressionAttributeValues = updateItem
    }
    return req
  }
  
  suspend fun getAllExercises(user: User): MutableList<Exercise> {
    val getItemRequest = GetItemRequest {
      tableName = USERDB_NAME
      key = user.getCurrentUserKeyInDB()
    }
    val ddb = DynamoDbClient { region = "ap-east-1" }
    val exerciseLogsInDB: MutableList<Exercise> = mutableListOf()
    try {
      val res = ddb.getItem(getItemRequest)
      val logObtained = res.item?.get("exercise_log")?.asL()
      logObtained?.forEach { currentList ->
        val returnMap = currentList.asM()
        val date = returnMap["date"]?.asS() ?: ""
        val name = returnMap["name"]?.asS() ?: ""
        val length = returnMap["length"]?.asN()?.toUInt() ?: 0u
        val calories = returnMap["calories"]?.asN()?.toUInt() ?: 0u
        val newExercise = Exercise(date, name, length, calories)
        exerciseLogsInDB.add(newExercise)
      }
    } catch (_: Exception) {
    }
    return exerciseLogsInDB
  }
}
