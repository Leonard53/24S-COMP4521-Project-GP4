package com.comp4521_project_gp4.model

class ExerciseModel {
    data class ExerciseItem(
        val exerciseType: String,
        val exerciseDateTime: String,
        val exerciseCalories: Int
    )
}