package com.example.restcountriesapp.core.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean

object AdsManager {

    private const val TAG = "AdsManager"

    private val consentRequestStarted = AtomicBoolean(false)
    private val mobileAdsInitializationStarted = AtomicBoolean(false)

    private val _canShowAds = MutableStateFlow(false)
    val canShowAds = _canShowAds.asStateFlow()

    fun initialize(activity: Activity) {
        if (!consentRequestStarted.compareAndSet(false, true)) {
            return
        }

        val consentInformation =
            UserMessagingPlatform.getConsentInformation(activity.applicationContext)

        val requestParameters = ConsentRequestParameters.Builder()
            // RestCountriesApp nie jest aplikacją skierowaną do dzieci.
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            requestParameters,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(
                            TAG,
                            "Consent form error: ${formError.errorCode} ${formError.message}"
                        )
                    }

                    if (consentInformation.canRequestAds()) {
                        initializeMobileAds(activity.applicationContext)
                    } else {
                        Log.d(TAG, "Consent does not currently allow ads")
                    }
                }
            },
            { requestError ->
                Log.w(
                    TAG,
                    "Consent request error: ${requestError.errorCode} ${requestError.message}"
                )

                // Poprzednia poprawna zgoda może nadal być zapisana.
                if (consentInformation.canRequestAds()) {
                    initializeMobileAds(activity.applicationContext)
                }
            }
        )
    }

    private fun initializeMobileAds(context: Context) {
        if (!mobileAdsInitializationStarted.compareAndSet(false, true)) {
            return
        }

        Thread {
            MobileAds.initialize(context) { initializationStatus ->
                initializationStatus.adapterStatusMap.forEach { (adapter, status) ->
                    Log.d(
                        TAG,
                        "Adapter: $adapter, state: ${status.initializationState}"
                    )
                }

                _canShowAds.value = true
                Log.d(TAG, "Google Mobile Ads initialized")
            }
        }.start()
    }
}