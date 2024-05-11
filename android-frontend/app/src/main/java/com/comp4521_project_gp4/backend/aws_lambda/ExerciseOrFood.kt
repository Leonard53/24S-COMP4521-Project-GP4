package com.comp4521_project_gp4.backend.aws_lambda

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest

abstract class ExerciseOrFood {
  var uuid = ""
  abstract fun convertToMap(): MutableMap<String, AttributeValue>
  abstract suspend fun updateRequest(user: User): UpdateItemRequest
}