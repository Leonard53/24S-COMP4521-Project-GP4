package com.comp4521_project_gp4.backend.aws_lambda

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest

data class Food(
  val date: String,
  val foodName: String,
  val foodCalories: UInt
) : ExerciseOrFood() {
  override fun convertToMap(): MutableMap<String, AttributeValue> {
    val returnMap = mutableMapOf<String, AttributeValue>()
    returnMap["date"] = AttributeValue.S(date)
    returnMap["name"] = AttributeValue.S(foodName)
    returnMap["calories"] = AttributeValue.N(foodCalories.toString())
    return returnMap
  }
  
  override suspend fun updateRequest(user: User): UpdateItemRequest {
    val updateItem = mutableMapOf<String, AttributeValue>()
    updateItem[":newItem"] = AttributeValue.M(convertToMap())
    val req = UpdateItemRequest {
      tableName = USERDB_NAME
      key = user.getCurrentUserKeyInDB()
      updateExpression = "SET exercise_log = list_append(exercise_log, :newItem)"
      expressionAttributeValues = updateItem
    }
    return req
  }
  
  suspend fun getAllFood(user: User): MutableList<Food> {
    val getItemRequest = GetItemRequest {
      tableName = USERDB_NAME
      key = user.getCurrentUserKeyInDB()
    }
    val ddb = DynamoDbClient { region = "ap-east-1" }
    val foodLogsInDB: MutableList<Food> = mutableListOf()
    
    try {
      val res = ddb.getItem(getItemRequest)
      val logObtained = res.item?.get("food_log")?.asL()
      logObtained?.forEach { currentList ->
        val returnMap = currentList.asM()
        val date = returnMap["date"]?.asS() ?: ""
        val name = returnMap["name"]?.asS() ?: ""
        val calories = returnMap["calories"]?.asN()?.toUInt() ?: 0u
        val newExercise = Food(date, name, calories)
        foodLogsInDB.add(newExercise)
      }
    } catch (_: Exception) {
    }
    return foodLogsInDB
  }
}