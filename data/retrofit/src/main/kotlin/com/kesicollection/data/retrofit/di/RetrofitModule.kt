package com.kesicollection.data.retrofit.di

import com.kesicollection.data.api.QuestionApi
import com.kesicollection.data.retrofit.RetrofitQuestionApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {

    //TODO: Let the app module define this.
    @Binds
    @Named("retrofit_question_api")
    abstract fun bindRetrofitQuestionApi(
        impl: RetrofitQuestionApi
    ): QuestionApi
}