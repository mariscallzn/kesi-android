package com.kesicollection.articles.di

import coil3.ImageLoader
import com.kesicollection.core.app.qualifiers.ArticlesAdKey
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Qualifier annotation for the [ImageLoader] instance provided through the
 * [ArticlesEntryPoint].
 *
 * Use this annotation to differentiate the [ImageLoader] provided by the
 * [ArticlesEntryPoint] from other [ImageLoader] instances that might be
 * available in the dependency graph.
 *
 * This is useful when you have multiple image loaders and want to specify
 * which one should be injected.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticlesEntryPointImageLoader

/**
 * This is an entry point for the Articles feature module.
 *
 * It provides dependencies that are scoped to the application's singleton component.
 * Feature modules that need to access these dependencies should obtain an instance
 * of this entry point using `@Inject` or the [EntryPointAccessors] helper.
 *
 * Example of accessing dependencies from a Feature module:
 *
 * ```kotlin
 * @AndroidEntryPoint
 * class SomeFeatureFragment : Fragment() {
 *
 *     @Inject lateinit var articlesEntryPoint: ArticlesEntryPoint
 *
 *     override fun onCreateView(
 *         inflater: LayoutInflater, container: ViewGroup?,
 *         savedInstanceState: Bundle?
 *     ): View? {
 *         val imageLoader = articlesEntryPoint.imageLoader()
 *         val adKey = articlesEntryPoint.articlesAdKey()
 *         // Use the dependencies
 *         ...
 *         return super.onCreateView(inflater, container, savedInstanceState)
 *     }
 * }
 * ```
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ArticlesEntryPoint {
    @ArticlesEntryPointImageLoader
    fun imageLoader(): ImageLoader

    @ArticlesAdKey
    fun articlesAdKey(): String
}