package com.comp4521_project_gp4.backend.aws_lambda

import android.os.Parcelable
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import aws.smithy.kotlin.runtime.util.toNumber
import kotlinx.parcelize.Parcelize
import java.util.UUID.randomUUID

@Parcelize
data class Exercise(
  val date: String,
  val exerciseName: String,
  val exerciseLengthInMins: UInt,
  val calories: UInt
) : ExerciseOrFood(), Parcelable {
  override fun convertToMap(): MutableMap<String, AttributeValue> {
    val returnMap = mutableMapOf<String, AttributeValue>()
    returnMap["uuid"] = AttributeValue.S(randomUUID().toString())
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
  
  companion object {
    suspend fun getAllExercises(user: User): MutableList<Exercise> {
      val getItemRequest = GetItemRequest {
        tableName = USERDB_NAME
        key = user.getCurrentUserKeyInDB()
      }
      try {
        val res = ddb.getItem(getItemRequest)
        val logObtained = res.item?.get("exercise_log")?.asL() ?: emptyList()
        return getAllExercises(logObtained)
      } catch (_: Exception) {
        return emptyList<Exercise>().toMutableList()
      }
    }
    
    fun getAllExercises(rawLog: List<AttributeValue>): MutableList<Exercise> {
      val returnList = mutableListOf<Exercise>()
      rawLog.forEach { currentList ->
        val returnMap = currentList.asM()
        val uuid = returnMap["uuid"]?.asS() ?: ""
        val date = returnMap["date"]?.asS() ?: ""
        val name = returnMap["name"]?.asS() ?: ""
        val length = returnMap["length"]?.asN()?.toUInt() ?: 0u
        val calories = returnMap["calories"]?.asN()?.toUInt() ?: 0u
        val newExercise = Exercise(date, name, length, calories)
        newExercise.uuid = uuid
        returnList.add(newExercise)
      }
      return returnList
    }
  }
}
