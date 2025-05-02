package com.kesicollection.kesiandroid.di

import com.kesicollection.core.app.AppManager
import com.kesicollection.core.app.Logger
import com.kesicollection.kesiandroid.KesiAndroidAppManager
import com.kesicollection.kesiandroid.logger.AndroidLogger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindsAppManager(
        kesiAndroidAppManager: KesiAndroidAppManager
    ): AppManager

    companion object {

        @Provides
        @Singleton
        fun providesLogger(): Logger = AndroidLogger
    }
}