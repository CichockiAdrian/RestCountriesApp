package com.example.restcountriesapp.core.error

import androidx.annotation.StringRes
import com.example.restcountriesapp.R

object ErrorMessageMapper {

    @StringRes
    fun toMessageRes(errorCode: String?): Int {
        return when (errorCode) {
            ErrorCode.NETWORK_ERROR -> R.string.error_network
            ErrorCode.OFFLINE_MODE -> R.string.error_offline_mode
            ErrorCode.API_KEY_ERROR -> R.string.error_api_key
            ErrorCode.SERVER_ERROR -> R.string.error_server
            ErrorCode.COUNTRY_NOT_FOUND -> R.string.error_country_not_found
            ErrorCode.INVALID_COUNTRY_CODE -> R.string.error_invalid_country_code
            ErrorCode.WIKI_INFO_NOT_FOUND -> R.string.error_wiki_unavailable
            else -> R.string.error_unknown
        }
    }
}