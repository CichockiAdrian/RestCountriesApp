package com.example.restcountriesapp.core.ads

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.restcountriesapp.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

private const val TAG = "AdaptiveBannerAd"

@Composable
fun AdaptiveBannerAd(
    modifier: Modifier = Modifier
) {
    val canShowAds by AdsManager.canShowAds.collectAsState()

    if (!canShowAds) {
        return
    }

    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    /*
     * Po zmianie orientacji szerokość się zmieni, więc tworzymy nowy AdView
     * z poprawnym rozmiarem adaptacyjnym.
     */
    key(screenWidthDp) {
        val adView = remember {
            AdView(context).apply {
                adUnitId = BuildConfig.ADMOB_BANNER_ID

                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        screenWidthDp
                    )
                )

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Banner loaded")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(
                            TAG,
                            "Banner failed: code=${error.code}, message=${error.message}"
                        )
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "Banner impression")
                    }

                    override fun onAdClicked() {
                        Log.d(TAG, "Banner clicked")
                    }
                }

                loadAd(
                    AdRequest.Builder()
                        .build()
                )
            }
        }

        DisposableEffect(adView) {
            onDispose {
                adView.destroy()
                Log.d(TAG, "Banner destroyed")
            }
        }

        AndroidView(
            factory = { adView },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}