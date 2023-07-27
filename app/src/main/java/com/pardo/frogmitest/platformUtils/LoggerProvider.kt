package com.pardo.frogmitest.platformUtils

class LoggerProvider {

    interface Logger {
        fun log(tag : String, message : String)
    }

    companion object{
        private lateinit var logger : Logger

        fun setLogger(logger : Logger){
            this.logger = logger
        }

        fun getLogger() : Logger? {
            if(this::logger.isInitialized){
                return logger
            }
            return logger
        }

    }
}