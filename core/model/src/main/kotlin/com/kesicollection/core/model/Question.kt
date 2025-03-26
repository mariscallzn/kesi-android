package com.kesicollection.core.model

import java.util.UUID

data class Question(
    val id: String = UUID.randomUUID().toString(),
    val questionType: QuestionType,
    val content: String,
    val options: List<String>,
    val topic: Topic,
    val correctAnswerIndex: Int, // Index of the correct answer in the options list
    val difficulty: Difficulty,
    val explanation: String? = null, // Optional: Explanation of the correct answer
    val tags: List<Tag> = emptyList(), // Optional: Tags for categorizing questions
)

enum class QuestionType {
    Text, Image, CodeSnippet
}

enum class Difficulty {
    Easy, Medium, Hard
}

