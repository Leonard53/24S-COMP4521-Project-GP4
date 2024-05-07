package com.comp4521_project_gp4.backend.aws_lambda

val EXECERCISE_TABLE_NAME = "comp4521-exercise-db"

data class Exercise(
  val date: String,
  val exerciseName: String,
  val exerciseLengthInMins: UInt,
  val calories: UInt
)
