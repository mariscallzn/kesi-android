package com.kesicollection.feature.quiz

import com.kesicollection.core.model.Question
import com.kesicollection.data.api.QuestionApi
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface QuizRepository {
    suspend fun fetchQuestionByTopicName(name: String): Result<List<Question>>
}

@Singleton
class QuizRepositoryImpl @Inject constructor(
    @Named("room_question_api")
    private val questionApi: QuestionApi
) : QuizRepository {
    override suspend fun fetchQuestionByTopicName(name: String): Result<List<Question>> =
        Result.runCatching {
            questionApi.fetchQuestions().getOrThrow()
                .filter { it.topic.name == name }
                .groupBy { it.topic }
                .map { it.value }
                .flatten()
        }
}