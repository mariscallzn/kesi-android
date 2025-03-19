package com.kesicollection.data.repository

import com.kesicollection.core.model.Topic
import com.kesicollection.data.api.QuestionApi
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface QuizTopicsRepository {
    suspend fun fetchTopics(): Result<List<Topic>>
}

@Singleton
class QuizTopicsRepositoryImpl @Inject constructor(
    @Named("retrofit_question_api")
    private val questionApi: QuestionApi,
) : QuizTopicsRepository {
    override suspend fun fetchTopics(): Result<List<Topic>> = Result.runCatching {
        questionApi.fetchQuestions().getOrThrow().groupBy {
            it.topic
        }.map { pair ->
            Topic(name = pair.key)
        }
    }
}