package com.pardo.frogmitest

import android.app.Application
import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.platformUtils.AndroidRestPlatformDependencies
import com.pardo.frogmitest.platformUtils.LoggerAndroid
import com.pardo.frogmitest.platformUtils.LoggerProvider

class FrogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        RestClient.setPlatformDependencies(AndroidRestPlatformDependencies())
        LoggerProvider.setLogger(LoggerAndroid())

    }
}