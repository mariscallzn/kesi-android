package com.kesicollection.feature.quiz

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuizModule {

    @Binds
    abstract fun bindRepositoryModule(
        impl: QuizRepositoryImpl
    ): QuizRepository

    companion object {

        @Singleton
        @Provides
        fun providesUiQuestionTypeFactory(): UIQuestionTypeFactory = DefaultUIQuestionTypeFactory()
    }
}