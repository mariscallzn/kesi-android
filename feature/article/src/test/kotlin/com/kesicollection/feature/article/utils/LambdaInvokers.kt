package com.kesicollection.feature.article.utils

import com.kesicollection.feature.article.uimodel.UiPodcast

fun interface OnPodcastClickLambda {
    operator fun invoke(uiPodcast: UiPodcast)
}

fun interface OnNavigateUpLambda {
    operator fun invoke()
}

fun interface OnTryAgainLambda {
    operator fun invoke()
}

fun interface OnPodcastIdClickLambda {
    operator fun invoke(id: String)
}