package com.kesicollection.feature.discover.di

import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.feature.discover.Intent
import com.kesicollection.feature.discover.UiState
import com.kesicollection.feature.discover.intentprocessor.DefaultIntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoverModule {

    @Binds
    @Singleton
    abstract fun bindIntentProcessorFactory(
        defaultIntentProcessorFactory: DefaultIntentProcessorFactory,
    ): IntentProcessorFactory<UiState, Intent>
}

