package com.kesicollection.core.uisystem.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.kesicollection.core.uisystem.LocalApp

object KAdView {
    const val TEST_TAG = ":core:uisystem:AdView"
}

@SuppressLint("MissingPermission")
@Composable
fun KAdView(
    modifier: Modifier = Modifier,
    adUnitId: String,
    screenName: String
) {
    val context = LocalContext.current
    val analytics = LocalApp.current.analytics
    AndroidView(
        modifier = modifier
            .testTag(KAdView.TEST_TAG),
        factory = {
            AdView(context).apply {
                setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                this.adUnitId = adUnitId
                this.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        analytics.logEvent(
                            analytics.event.adImpression, mapOf(
                                analytics.param.screenName to screenName
                            )
                        )
                    }
                }
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}