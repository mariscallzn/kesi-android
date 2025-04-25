package com.kesicollection.kesiandroid.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.kesicollection.core.analytics.AnalyticsWrapper
import com.kesicollection.kesiandroid.analytics.FirebaseAnalyticsWrapper
import com.kesicollection.kesiandroid.analytics.FirebaseEventProvider
import com.kesicollection.kesiandroid.analytics.FirebaseParamProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsWrapper(
        firebaseImpl: FirebaseAnalyticsWrapper
    ): AnalyticsWrapper

    companion object {

        @Provides
        @Singleton
        fun providesFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

        @Provides
        @Singleton
        fun providesFirebaseAnalyticsWrapperEventProvider(): AnalyticsWrapper.Event =
            FirebaseEventProvider

        @Provides
        @Singleton
        fun providesFirebaseAnalyticsWrapperParamProvider(): AnalyticsWrapper.Param =
            FirebaseParamProvider
    }
}