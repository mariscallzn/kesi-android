package com.kesicollection.data.retrofit

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.retrofit.fake.FakeNetworkDiscover
import com.kesicollection.data.retrofit.model.kesiandroid.asDiscover
import com.kesicollection.data.retrofit.service.KesiAndroidService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RetrofitDiscoverApiTest {
    private val mockKesiAndroidService: KesiAndroidService = mockk()
    private lateinit var retrofitDiscoverApi: RetrofitDiscoverApi

    @Before
    fun setUp() {
        retrofitDiscoverApi = RetrofitDiscoverApi(
            kesiAndroidService = mockKesiAndroidService
        )
    }

    @Test
    fun `getDiscoverContent returns success when service returns data`() = runTest {
        // Given
        val mockNetworkDiscover = FakeNetworkDiscover.items.first()
        val expectedDiscover = mockNetworkDiscover.asDiscover()

        coEvery { mockKesiAndroidService.getDiscoverContent() } returns mockNetworkDiscover

        // When
        val result = retrofitDiscoverApi.getDiscoverContent()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedDiscover)
    }

    @Test
    fun `getDiscoverContent returns failure when service throws exception`() = runTest {
        // Given
        val expectedException = IOException("Network error")
        coEvery { mockKesiAndroidService.getDiscoverContent() } throws expectedException

        // When
        val result = retrofitDiscoverApi.getDiscoverContent()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
    }

}