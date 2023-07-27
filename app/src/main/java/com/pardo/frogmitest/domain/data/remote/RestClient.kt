package com.pardo.frogmitest.domain.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RestClient {

    interface PlatformDependencies {
        fun validateUrl(url : String) : Boolean
    }
    companion object{
        private var client = OkHttpClient()
        private lateinit var platformCheck : PlatformDependencies

        fun setPlatformDependencies(dep : PlatformDependencies){
            this.platformCheck = dep
        }

        fun <T> getData(url: String, headers: Map<String, String>): Flow<T> {

            if(this::platformCheck.isInitialized){
                Error("Network error. Platform check not provided.")
            }

            var flow = flow<T> {

                val builder = Request.Builder()
                headers.forEach {
                    builder.addHeader(it.key, it.value)
                }
                builder.url(url)
                client.newCall(builder.build()).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call, response: Response) {

                    }
                })

            }.flowOn(Dispatchers.IO)
            return flow
        }
    }
}