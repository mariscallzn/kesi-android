package com.kesicollection.data.retrofit

import com.kesicollection.core.model.Discover
import com.kesicollection.data.api.DiscoverApi
import com.kesicollection.data.retrofit.model.kesiandroid.asDiscover
import com.kesicollection.data.retrofit.service.KesiAndroidService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [DiscoverApi] implementation that uses Retrofit to fetch discover content.
 */
@Singleton
class RetrofitDiscoverApi @Inject constructor(
    private val kesiAndroidService: KesiAndroidService,
) : DiscoverApi {
    override suspend fun getDiscoverContent(): Result<Discover> = Result.runCatching {
        kesiAndroidService.getDiscoverContent().asDiscover()
    }
}