package com.pardo.frogmitest

import com.pardo.frogmitest.platformUtils.LoggerProvider

class LoggerJVM : LoggerProvider.Logger{
    override fun log(tag: String, message: String) {
        if(tag!="") {
            println("$tag::$message")
        } else {
            println(message)
        }
    }
}