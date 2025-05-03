package com.kesicollection.kesiandroid.di

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.kesiandroid.crashlytics.FirebaseCrashlyticsProvider
import com.kesicollection.kesiandroid.crashlytics.FirebaseCrashlyticsWrapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashlyticsModule {

    @Binds
    @Singleton
    abstract fun bindCrashlyticsWrapper(
        firebaseCrashlyticsWrapper: FirebaseCrashlyticsWrapper
    ): CrashlyticsWrapper

    companion object {

        @Provides
        @Singleton
        fun providesFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

        @Provides
        @Singleton
        fun providesFirebaseCrashlyticsProvider(): CrashlyticsWrapper.Params =
            FirebaseCrashlyticsProvider
    }
}