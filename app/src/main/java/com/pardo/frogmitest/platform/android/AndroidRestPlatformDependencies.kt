package com.pardo.frogmitest.platform.android

import android.webkit.URLUtil
import com.pardo.frogmitest.domain.data.remote.RestClient

class AndroidRestPlatformDependencies : RestClient.PlatformDependencies {
    override fun validateUrl(url: String): Boolean {
        return URLUtil.isValidUrl(url)
    }
}