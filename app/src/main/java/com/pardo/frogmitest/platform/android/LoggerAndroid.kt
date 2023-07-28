package com.pardo.frogmitest.platform.android

import android.util.Log
import com.pardo.frogmitest.platform.LoggerProvider

class LoggerAndroid : LoggerProvider.Logger {
    override fun log(tag: String, message: String) {
        Log.e(tag, message)
    }
}