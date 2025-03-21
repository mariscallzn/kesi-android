package com.kesicollection.feature.quiztopics

sealed interface QuizTopicsIntent {
    data object FetchTopics: QuizTopicsIntent
}