package com.kesicollection.feature.quiztopics

sealed interface QuizTopicsUiEvent {
    data object FetchTopics: QuizTopicsUiEvent
}