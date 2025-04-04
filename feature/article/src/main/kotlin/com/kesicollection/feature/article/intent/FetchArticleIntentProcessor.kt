package com.kesicollection.feature.article.intent

import com.kesicollection.core.model.ContentSection
import com.kesicollection.data.usecase.GetArticleByIdUseCase
import com.kesicollection.feature.article.Reducer
import com.kesicollection.feature.article.components.BulletList
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.components.Paragraph
import com.kesicollection.feature.article.components.SubHeader
import com.kesicollection.feature.article.uimodel.UiPodcast
import kotlinx.coroutines.delay

class FetchArticleIntentProcessor(
    private val articleId: String,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true) }
        val result = getArticleByIdUseCase(articleId).getOrThrow()
        delay(1500)
        reducer {
            copy(
                isLoading = false,
                title = result.title,
                content = result.content.map {
                    when (it) {
                        is ContentSection.BulletList -> BulletList(it.content, it.bulletPoints)
                        is ContentSection.Code -> Code(it.content)
                        is ContentSection.Paragraph -> Paragraph(it.content)
                        is ContentSection.SubHeader -> SubHeader(it.content)
                    }
                },
                podcast = result.podcast?.let {
                    UiPodcast(
                        id = it.id,
                        title = it.title,
                        audioUrl = it.url
                    )
                }
            )
        }
    }
}