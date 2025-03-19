package com.kesicollection.data.repository.di

import com.kesicollection.data.repository.QuizRepository
import com.kesicollection.data.repository.QuizRepositoryImpl
import com.kesicollection.data.repository.QuizTopicsRepository
import com.kesicollection.data.repository.QuizTopicsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindQuizTopicsRepository(
        impl: QuizTopicsRepositoryImpl
    ): QuizTopicsRepository

    @Binds
    abstract fun bindQuizRepository(
        impl: QuizRepositoryImpl
    ): QuizRepository

}