package com.kesicollection.data.retrofit

import com.kesicollection.core.model.Question
import com.kesicollection.data.api.QuestionApi
import com.kesicollection.data.api.mockedQuestions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitQuestionApi @Inject constructor() : QuestionApi {
    override suspend fun fetchQuestions(): Result<List<Question>> = Result.runCatching {
        mockedQuestions()
    }
}