package com.kesicollection.feature.quiztopics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QuizTopicsModule {


    @Binds
    abstract fun bindQuizTopicsModule(
        impl: QuizTopicsRepositoryImpl
    ): QuizTopicsRepository
}