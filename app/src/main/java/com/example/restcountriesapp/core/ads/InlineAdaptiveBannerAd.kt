package com.example.restcountriesapp.core.ads

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.restcountriesapp.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

private const val TAG = "InlineAdaptiveBannerAd"
private const val MAX_AD_HEIGHT_DP = 120

@Composable
fun InlineAdaptiveBannerAd(
    modifier: Modifier = Modifier
) {
    val canShowAds by AdsManager.canShowAds.collectAsState()

    if (!canShowAds) {
        return
    }

    val context = LocalContext.current

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val availableWidthDp = maxWidth.value
            .toInt()
            .coerceAtLeast(1)

        val adView = remember(context, availableWidthDp) {
            AdView(context).apply {
                adUnitId = BuildConfig.ADMOB_BANNER_ID

                setAdSize(
                    AdSize.getInlineAdaptiveBannerAdSize(
                        availableWidthDp,
                        MAX_AD_HEIGHT_DP
                    )
                )

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Inline banner loaded")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(
                            TAG,
                            "Inline banner failed: " +
                                    "code=${error.code}, " +
                                    "message=${error.message}"
                        )
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "Inline banner impression")
                    }

                    override fun onAdClicked() {
                        Log.d(TAG, "Inline banner clicked")
                    }
                }

                loadAd(
                    AdRequest.Builder().build()
                )
            }
        }

        DisposableEffect(adView) {
            onDispose {
                adView.destroy()
                Log.d(TAG, "Inline banner destroyed")
            }
        }

        AndroidView(
            factory = { adView },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}