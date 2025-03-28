package com.kesicollection.data.retrofit.di

import com.kesicollection.data.api.DogImagesApi
import com.kesicollection.data.api.QuestionApi
import com.kesicollection.data.retrofit.RetrofitDogImagesApi
import com.kesicollection.data.retrofit.RetrofitQuestionApi
import com.kesicollection.data.retrofit.service.DogCeoService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {

    //TODO: Let the app module define this.
    @Binds
    @Named("retrofit_question_api")
    abstract fun bindRetrofitQuestionApi(
        impl: RetrofitQuestionApi
    ): QuestionApi

    //TODO: Let the app module define this.
    @Binds
    @Named("retrofit_dog_images_api")
    abstract fun bindDogImagesApi(
        impl: RetrofitDogImagesApi
    ): DogImagesApi

    companion object {
        /**
         * Provides a Json setting preset
         *
         * ignoreUnknownKeys is set to true to protect front-end from crashes due to unknown keys
         */
        @Singleton
        @Provides
        fun providesJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        /**
         * Provides an OkHttp [Call.Factory] instance.
         *
         * This factory is configured with a [HttpLoggingInterceptor] to log HTTP request and response bodies.
         * The logging level is set to [HttpLoggingInterceptor.Level.BODY] for detailed logging.
         * This allows for observing the data being sent and received by the network calls,
         * which is very useful for debugging purposes.
         *
         * @return A configured [Call.Factory] instance.
         */
        @Singleton
        @Provides
        fun providesOkHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }).build()

        /**
         * Provides a [DogCeoService] instance for interacting with the Dog CEO API.
         *
         * This function constructs a Retrofit client configured to communicate with the Dog CEO API.
         * It utilizes the provided [Json] instance for JSON serialization/deserialization and a
         * [Call.Factory] for executing network requests.
         *
         * @param json The [Json] instance used for parsing JSON responses.
         * @param callFactory The [Call.Factory] used to create network call instances.
         * @return A fully configured [DogCeoService] instance.
         */
        @Singleton
        @Provides
        fun providesDogCeoService(json: Json, callFactory: Call.Factory): DogCeoService =
            Retrofit.Builder()
                .baseUrl("https://dog.ceo/api/")
                .callFactory { callFactory.newCall(it) }
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(DogCeoService::class.java)
    }
}