package com.kesicollection.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides access to a [DataStore] for storing and retrieving app preferences.
 *
 * This property delegates the creation and management of the [DataStore] instance to
 * the [preferencesDataStore] delegate, simplifying the setup and providing a
 * convenient way to interact with the preferences.
 *
 * The data is stored in a file named "kesi_android" within the app's data directory.
 *
 * @see DataStore
 * @see preferencesDataStore
 * @see Preferences
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kesi_android")

/**
 * DataStoreModule
 *
 * This Hilt module provides a singleton instance of the DataStore<Preferences> for the application.
 * It uses the preferencesDataStore delegate to simplify DataStore setup and provides a
 * centralized way to access DataStore throughout the application.
 *
 * DataStore is a data storage solution that allows you to store key-value pairs or typed objects
 * asynchronously and transactionally. It is well-suited for storing small to medium amounts of data.
 *
 * The DataStore instance provided by this module is scoped to the application's lifecycle,
 * ensuring that it is available throughout the application's lifetime.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class DataStoreModule {
    companion object {
        @Provides
        @Singleton
        fun providesDataStore(
            @ApplicationContext
            context: Context
        ): DataStore<Preferences> = context.dataStore
    }
}