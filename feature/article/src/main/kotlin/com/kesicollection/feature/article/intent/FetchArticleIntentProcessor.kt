package com.kesicollection.feature.article.intent

import com.kesicollection.core.uisystem.component.ContentType
import com.kesicollection.feature.article.Reducer
import com.kesicollection.feature.article.components.BulletList
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.components.Paragraph
import com.kesicollection.feature.article.components.SubHeader
import kotlinx.coroutines.delay

class FetchArticleIntentProcessor(
    private val articleId: String,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true) }
        delay(1500) //Fetch from network
        reducer {
            copy(
                isLoading = false,
                title = "This is the loaded content $articleId",
                content = testData
            )
        }
    }
}

val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

val testData = listOf<ContentType>(
    SubHeader("Subheader example with more data to test"),
    Paragraph(lorem.take(100)),
    BulletList(
        "Bullet list example with more data to test",
        listOf(lorem.take(150), lorem, lorem.take(80), lorem.take(30), lorem)
    ),
    Code("Code example with more data to test"),
    Paragraph(lorem),
)