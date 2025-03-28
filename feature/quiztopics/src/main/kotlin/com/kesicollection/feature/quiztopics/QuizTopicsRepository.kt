package com.kesicollection.feature.quiztopics

import com.kesicollection.core.model.Topic
import com.kesicollection.core.model.TopicCard
import com.kesicollection.data.api.QuestionApi
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface QuizTopicsRepository {
    suspend fun fetchTopics(): Result<List<TopicCard>>
}

@Singleton
class QuizTopicsRepositoryImpl @Inject constructor(
    @Named("retrofit_question_api")
    private val questionApi: QuestionApi,
) : QuizTopicsRepository {
    override suspend fun fetchTopics(): Result<List<TopicCard>> = Result.runCatching {
        questionApi.fetchQuestions().getOrThrow().groupBy {
            it.topic.name
        }.map { pair ->
            TopicCard(
                topic = Topic(name = pair.key),
                totalQuestions = pair.value.size
            )
        }
    }
}