package com.kesicollection.feature.quiz

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QuizModule {

    @Binds
    abstract fun bindRepositoryModule(
        impl: QuizRepositoryImpl
    ): QuizRepository
}