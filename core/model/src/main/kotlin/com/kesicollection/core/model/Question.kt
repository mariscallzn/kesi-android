package com.kesicollection.core.model

import java.util.UUID

//TODO: Basic data class, use types in the future instead of just strings.
data class Question(
    val id: String = UUID.randomUUID().toString(),
    val question: String,
    val options: List<String>,
    val topic: String,
    val correctAnswerIndex: Int, // Index of the correct answer in the options list
    val difficulty: String = "Medium", // Optional: Difficulty level (Easy, Medium, Hard)
    val explanation: String? = null, // Optional: Explanation of the correct answer
    val questionType: String = "MultipleChoice", // Optional: Type of question (MultipleChoice, TrueFalse, etc.)
    val tags: List<String> = emptyList(), // Optional: Tags for categorizing questions
)

