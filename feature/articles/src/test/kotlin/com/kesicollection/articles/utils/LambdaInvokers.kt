package com.kesicollection.articles.utils

import com.kesicollection.articles.Intent
import com.kesicollection.articles.model.UiArticle

// This interface is built to avoid Robolectric and Mockk to duplicate class definitions on sdk
// version 23, for other versions we could use just mockk regular lambdas. But we need to test 23
interface OnArticleClick {
    operator fun invoke(article: UiArticle)
}

// For onArticleClick: (String) -> Unit
fun interface OnArticleScreenArticleClick {
    operator fun invoke(articleId: String)
}

// For onBookmarkClick: (Intent.BookmarkClicked) -> Unit
fun interface OnArticleScreenBookmarkClick {
    operator fun invoke(intent: Intent.BookmarkClicked)
}

// For onTryAgain: (Intent.FetchArticles) -> Unit
fun interface OnTryAgain {
    operator fun invoke(intent: Intent.FetchArticles)
}

// For onNavigateUp: () -> Unit
fun interface OnNavigateUp {
    operator fun invoke()
}

fun interface OnBookmarkClick {
    operator fun invoke(id: String)
}
