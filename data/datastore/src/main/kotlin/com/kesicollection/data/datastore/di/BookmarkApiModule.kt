package com.kesicollection.data.datastore.di

import com.kesicollection.data.api.BookmarkApi
import com.kesicollection.data.datastore.DataStoreBookmarkApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module providing [BookmarkApi] dependencies for the application.
 *
 * This module is installed in the [SingletonComponent], making the bound [BookmarkApi]
 * instance available throughout the application as a singleton.
 *
 * It uses the [Binds] annotation to define an abstract binding, allowing Hilt to
 * automatically provide the concrete implementation of [BookmarkApi] which is
 * [DataStoreBookmarkApi].
 *
 * This approach promotes loose coupling and makes it easier to swap implementations
 * if needed (e.g., for testing or different data sources).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkApiModule {

    @Binds
    @Singleton
    abstract fun bindBookmarkApi(
        bookmarkApi: DataStoreBookmarkApi
    ): BookmarkApi
}