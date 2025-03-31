package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.IntentProcessor
import com.kesicollection.articles.Reducer
import com.kesicollection.articles.model.UiArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchArticlesIntentProcessor @Inject constructor(

) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer {
            copy(
                articles = listOf(
                    UiArticle(
                        title = "Article 1",
                        description = "Description 1",
                        articleId = "2",
                        thumbnail = "https://images.ctfassets.net/23aumh6u8s0i/4JQe27JmEip0X21uRGa0sr/ae23bfa547ed18a019c6fd92615f1eff/jetpack_compose_icon.png",
                    ),
                    UiArticle(
                        title = "Article 1",
                        description = "Description 1",
                        articleId = "1",
                        thumbnail = "https://images.ctfassets.net/23aumh6u8s0i/4JQe27JmEip0X21uRGa0sr/ae23bfa547ed18a019c6fd92615f1eff/jetpack_compose_icon.png",
                    )
                )
            )
        }
    }
}