package com.kesicollection.feature.doggallery

import coil3.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageLoaderEntryPoint {
    fun imageLoader(): ImageLoader
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DogGalleryModule {

    /**
     * Binds the concrete implementation [DogGalleryRepositoryImpl] to the [DogGalleryRepository] interface.
     *
     * This provides a way for dependency injection to resolve dependencies on [DogGalleryRepository]
     * with an instance of [DogGalleryRepositoryImpl].
     *
     * This binding is scoped to the [SingletonComponent], ensuring a single instance of the repository
     * is used throughout the application.
     *
     * @param impl The concrete implementation of the dog gallery repository.
     * @return An instance of [DogGalleryRepository].
     */
    @Singleton
    @Binds
    abstract fun bindDogGalleryRepository(
        impl: DogGalleryRepositoryImpl
    ): DogGalleryRepository
}