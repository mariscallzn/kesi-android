package com.kesicollection.domain

import com.kesicollection.data.repository.ArticleRepository
import com.kesicollection.data.repository.ArticleRepositoryImpl
import com.kesicollection.data.repository.BookmarkRepository
import com.kesicollection.data.repository.BookmarkRepositoryImpl
import com.kesicollection.data.repository.DiscoverRepository
import com.kesicollection.data.repository.DiscoverRepositoryImpl
import com.kesicollection.data.repository.MarkdownRepository
import com.kesicollection.data.repository.MarkdownRepositoryImpl
import com.kesicollection.data.repository.PodcastRepository
import com.kesicollection.data.repository.PodcastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepository: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindDiscoverRepository(
        impl: DiscoverRepositoryImpl
    ): DiscoverRepository

    @Binds
    @Singleton
    abstract fun bindPodcastRepository(
        impl: PodcastRepositoryImpl
    ): PodcastRepository

    @Binds
    @Singleton
    abstract fun bindMarkdownRepository(
        impl: MarkdownRepositoryImpl
    ): MarkdownRepository
}