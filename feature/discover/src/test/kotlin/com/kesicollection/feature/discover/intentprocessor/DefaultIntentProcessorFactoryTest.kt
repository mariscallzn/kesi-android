package com.kesicollection.feature.discover.intentprocessor

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.domain.GetDiscoverContentUseCase
import com.kesicollection.feature.discover.Intent
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 * Tests for [DefaultIntentProcessorFactory].
 *
 * This class tests the following:
 * - [DefaultIntentProcessorFactory.create] with [Intent.FetchFeatureItems] returns an instance of [FetchFeatureItemsIntentProcessor].
 * - The returned [FetchFeatureItemsIntentProcessor] is correctly initialized with the provided dependencies ([GetDiscoverContentUseCase] and [CrashlyticsWrapper]).
 */
class DefaultIntentProcessorFactoryTest {

    /**
     * Mocked instance of [GetDiscoverContentUseCase].
     */
    private lateinit var getDiscoverContentUseCase: GetDiscoverContentUseCase
    /**
     * Mocked instance of [CrashlyticsWrapper].
     * `relaxed = true` is used to avoid stubbing every method, as we are only interested in verifying its presence.
     */
    private lateinit var crashlyticsWrapper: CrashlyticsWrapper
    /**
     * Instance of [DefaultIntentProcessorFactory] under test.
     */
    private lateinit var factory: DefaultIntentProcessorFactory

    /**
     * Sets up the test environment before each test.
     * Initializes the mocks and the factory instance.
     */
    @Before
    fun setUp() {
        getDiscoverContentUseCase = mockk()
        crashlyticsWrapper = mockk(relaxed = true) // relaxed = true to avoid stubbing every method
        factory = DefaultIntentProcessorFactory(getDiscoverContentUseCase, crashlyticsWrapper)
    }

    /**
     * Tests that when [DefaultIntentProcessorFactory.create] is called with an [Intent.FetchFeatureItems],
     * it returns an instance of [FetchFeatureItemsIntentProcessor].
     * It also verifies that the returned processor is initialized with the correct dependencies.
     */
    @Test
    fun `create with FetchFeatureItems intent returns FetchFeatureItemsIntentProcessor`() {
        // Given
        val intent = Intent.FetchFeatureItems

        // When
        val processor = factory.create(intent)

        // Then
        assertThat(processor).isInstanceOf(FetchFeatureItemsIntentProcessor::class.java)
        val fetchProcessor = processor as FetchFeatureItemsIntentProcessor
        assertThat(fetchProcessor.getDiscoverContentUseCase).isEqualTo(getDiscoverContentUseCase)
        assertThat(fetchProcessor.crashlyticsWrapper).isEqualTo(crashlyticsWrapper)
    }
}