package com.kesicollection.feature.article

import com.kesicollection.feature.article.intent.DefaultIntentProcessorFactory
import com.kesicollection.feature.article.intent.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    @Binds
    @Singleton
    abstract fun bindInterProcessorFactory(
        impl: DefaultIntentProcessorFactory
    ): IntentProcessorFactory
}