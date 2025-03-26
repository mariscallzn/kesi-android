package com.kesicollection.feature.quiz

import com.kesicollection.core.model.Question
import com.kesicollection.feature.quiz.component.UIQuestionType

data class UiQuestion(
    val metadata: Question,
    val question: UIQuestionType
)
