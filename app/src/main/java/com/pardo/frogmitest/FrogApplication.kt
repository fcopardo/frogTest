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

        RestClient.setPlatformDependencies(AndroidRestPlatformDependencies())
        LoggerProvider.setLogger(LoggerAndroid())
        CredentialsProvider.getInstance().setToken(BuildConfig.AUTH)
        CredentialsProvider.getInstance().setCompanyId(BuildConfig.COMPANY_ID)
    }
}