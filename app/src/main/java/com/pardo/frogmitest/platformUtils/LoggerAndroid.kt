package com.pardo.frogmitest.platformUtils

import android.util.Log

class LoggerAndroid : LoggerProvider.Logger {
    override fun log(tag: String, message: String) {
        Log.e(tag, message)
    }
}