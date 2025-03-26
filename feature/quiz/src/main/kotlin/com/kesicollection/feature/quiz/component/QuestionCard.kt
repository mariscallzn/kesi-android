package com.kesicollection.feature.quiz.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kesicollection.core.model.Difficulty
import com.kesicollection.core.model.Question
import com.kesicollection.core.model.QuestionType
import com.kesicollection.core.model.Topic
import com.kesicollection.core.uisystem.component.AnsweredOption
import com.kesicollection.core.uisystem.component.SingleOption
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.colorAnimation
import com.kesicollection.core.uisystem.component.SingleOptionDefaults.trailingScaleAnimation
import com.kesicollection.core.uisystem.theme.KIcon
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.UiQuestion

interface UIQuestionType {
    @Composable
    fun Question(modifier: Modifier = Modifier)
}

class TextQuestion(private val question: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        Text(text = question, style = MaterialTheme.typography.bodyLarge)
    }
}

class ImageQuestion(private val question: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        // Placeholder for image question
        Text(text = "Image question: $question")
        // Image(painter = ..., contentDescription = null, modifier = modifier)
    }
}

class CodeSnippetQuestion(private val question: String) : UIQuestionType {
    @Composable
    override fun Question(modifier: Modifier) {
        // Placeholder for code snippet question
        Text(text = "Code Snippet question: $question")
        // Text(text = question, modifier = modifier)
    }
}

@Composable
fun DisplayQuestion(
    questionType: UIQuestionType,
    modifier: Modifier = Modifier,
) {
    questionType.Question(modifier)
}

@Composable
fun QuestionCard(
    question: UiQuestion,
    onSelectedAnswer: (questionId: String, selectedIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAnswer by rememberSaveable { mutableIntStateOf(-1) }
    Card(modifier = modifier.animateContentSize(), shape = MaterialTheme.shapes.large) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DisplayQuestion(question.question)
            Spacer(Modifier.height(24.dp))
            Text(text = "Choose the answer", style = MaterialTheme.typography.labelLarge)
            question.metadata.options.forEachIndexed { index, item ->
                val color by colorAnimation(
                    when {
                        selectedAnswer == index && selectedAnswer == question.metadata.correctAnswerIndex -> AnsweredOption.CORRECT
                        selectedAnswer == index && selectedAnswer >= 0 && selectedAnswer != question.metadata.correctAnswerIndex -> AnsweredOption.INCORRECT
                        else -> AnsweredOption.DEFAULT
                    }
                )
                val scale by trailingScaleAnimation(selectedAnswer == index && selectedAnswer >= 0)
                SingleOption(
                    trailing = {
                        when {
                            selectedAnswer == index && selectedAnswer == question.metadata.correctAnswerIndex ->
                                Icon(KIcon.Check, null)

                            selectedAnswer == index && selectedAnswer != question.metadata.correctAnswerIndex ->
                                Icon(KIcon.Cancel, null)
                        }
                    },
                    color = { color },
                    trailingScale = { scale },
                    onClick = {
                        selectedAnswer = if (selectedAnswer == index) -1 else index
                        onSelectedAnswer(question.metadata.id, index)
                    },
                ) {
                    Text(item, style = MaterialTheme.typography.bodyMedium)
                }
                if (index == question.metadata.options.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
            question.metadata.explanation?.let {
                if (selectedAnswer >= 0) {
                    Text(it, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewLightDarkQuestionCard() {
    Example(modifier = Modifier.padding(8.dp))
}

@Composable
private fun Example(modifier: Modifier = Modifier) {
    KesiTheme {
        KesiTheme {
            QuestionCard(
                question = UiQuestion(
                    Question(
                        id = "1",
                        content = "What is the capital of France?",
                        options = listOf("London", "Paris", "Berlin", "Madrid"),
                        topic = Topic(name = "Geography"),
                        correctAnswerIndex = 1,
                        questionType = QuestionType.Text,
                        difficulty = Difficulty.Easy,
                        explanation = "Paris is the capital of France.",
                    ), TextQuestion("What is the capital of France?")
                ),
                onSelectedAnswer = { _, _ -> },
                modifier = modifier,
            )
        }
    }
}