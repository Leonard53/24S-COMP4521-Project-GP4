package com.comp4521_project_gp4.backend.aws_lambda

import android.os.Parcelable
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Food(
  val date: String,
  val foodName: String,
  val foodCalories: UInt
) : ExerciseOrFood(), Parcelable {
  override fun convertToMap(): MutableMap<String, AttributeValue> {
    val returnMap = mutableMapOf<String, AttributeValue>()
    returnMap["uuid"] = AttributeValue.S(UUID.randomUUID().toString())
    returnMap["date"] = AttributeValue.S(date)
    returnMap["name"] = AttributeValue.S(foodName)
    returnMap["calories"] = AttributeValue.N(foodCalories.toString())
    return returnMap
  }
  
  override suspend fun updateRequest(user: User): UpdateItemRequest {
    val updateItem = mutableMapOf<String, AttributeValue>()
    updateItem[":newItem"] = AttributeValue.L(listOf(AttributeValue.M(convertToMap())))
    val req = UpdateItemRequest {
      tableName = USERDB_NAME
      key = user.getCurrentUserKeyInDB()
      updateExpression = "SET food_log = list_append(food_log, :newItem)"
      expressionAttributeValues = updateItem
    }
    return req
  }
  
  companion object {
    suspend fun getAllFood(user: User): MutableList<Food> {
      val getItemRequest = GetItemRequest {
        tableName = USERDB_NAME
        key = user.getCurrentUserKeyInDB()
      }
      try {
        val res = ddb.getItem(getItemRequest)
        val logObtained = res.item?.get("food_log")?.asL() ?: emptyList()
        return getAllFood(logObtained)
      } catch (_: Exception) {
        return emptyList<Food>().toMutableList()
      }
    }
    
    suspend fun getAllFood(rawLog: List<AttributeValue>): MutableList<Food> {
      val returnList = mutableListOf<Food>()
      rawLog.forEach { currentList ->
        val returnMap = currentList.asM()
        val uuid = returnMap["uuid"]?.asS() ?: ""
        val date = returnMap["date"]?.asS() ?: ""
        val name = returnMap["name"]?.asS() ?: ""
        val calories = returnMap["calories"]?.asN()?.toUInt() ?: 0u
        val newFood = Food(date, name, calories)
        newFood.uuid = uuid
        returnList.add(newFood)
      }
      return returnList
    }
  }
}