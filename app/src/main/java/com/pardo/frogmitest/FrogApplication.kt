package com.pardo.frogmitest

import android.app.Application
import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.data.secure.CredentialsProvider
import com.pardo.frogmitest.platform.android.AndroidRestPlatformDependencies
import com.pardo.frogmitest.platform.android.LoggerAndroid
import com.pardo.frogmitest.platform.LoggerProvider

class FrogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //if you don't want to set up environment variables for the token & company ID, set them
        // up here.
        RestClient.setPlatformDependencies(AndroidRestPlatformDependencies())
        LoggerProvider.setLogger(LoggerAndroid())
        CredentialsProvider.getInstance().setToken(BuildConfig.AUTH)
        CredentialsProvider.getInstance().setCompanyId(BuildConfig.COMPANY_ID)
    }
}