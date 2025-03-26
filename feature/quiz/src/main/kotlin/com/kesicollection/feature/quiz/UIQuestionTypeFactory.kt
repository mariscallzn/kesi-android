package com.kesicollection.feature.quiz

import com.kesicollection.core.model.Question
import com.kesicollection.core.model.QuestionType
import com.kesicollection.feature.quiz.component.CodeSnippetQuestion
import com.kesicollection.feature.quiz.component.ImageQuestion
import com.kesicollection.feature.quiz.component.TextQuestion
import com.kesicollection.feature.quiz.component.UIQuestionType

interface UIQuestionTypeFactory {
    fun create(question: Question): UIQuestionType
}

class DefaultUIQuestionTypeFactory : UIQuestionTypeFactory {
    override fun create(question: Question): UIQuestionType {
        return when (question.questionType) {
            QuestionType.Text -> TextQuestion(question.content)
            QuestionType.Image -> ImageQuestion(question.content)
            QuestionType.CodeSnippet -> CodeSnippetQuestion(question.content)
        }
    }
}