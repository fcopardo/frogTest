package com.pardo.frogmitest

import com.pardo.frogmitest.domain.data.remote.RestClient
import java.net.URL

class JVMRestPlatformDependencies : RestClient.PlatformDependencies {
    override fun validateUrl(url: String): Boolean {
        return try{
            URL(url).toURI()
            true
        }catch (e : Exception){
            false
        }
    }
}