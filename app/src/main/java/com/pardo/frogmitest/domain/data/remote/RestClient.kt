package com.pardo.frogmitest.domain.data.remote

import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.reflect.KClass

class RestClient {

    enum class ErrorType {
        ARGUMENT, SERVER, CREDENTIALS, CONNECTION, REDIRECT, RANDOM
    }
    sealed class NetworkResult<out T> {
        data class Success<out T>(val value: Any?) : NetworkResult<T>()
        data class Error(val code : Int, val type: ErrorType) : NetworkResult<Nothing>()
    }

    interface PlatformDependencies {
        fun validateUrl(url : String) : Boolean
    }
    companion object{
        private var client = OkHttpClient()
        private lateinit var platformCheck : PlatformDependencies

        fun setPlatformDependencies(dep : PlatformDependencies){
            this.platformCheck = dep
        }

        suspend fun <T : Any> getData(url: String, headers: Map<String, String> = mutableMapOf(), clazz: KClass<T>): Flow<NetworkResult<T>> {

            LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call started")

            if(this::platformCheck.isInitialized){
                Error("Network error. Platform check not provided.")
            }

            return callbackFlow<NetworkResult<T>> {

                LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call building")

                val builder = Request.Builder()
                headers.forEach {
                    builder.addHeader(it.key, it.value)
                }
                builder.url(url)
                var call = client.newCall(builder.build())
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call failed")
                        trySend(NetworkResult.Error(1, ErrorType.CONNECTION))
                    }
                    override fun onResponse(call: Call, response: Response) {
                        LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call reached the server")
                        if(response.isSuccessful){
                            response.body?.let { body ->
                                trySend(NetworkResult.Success<T>(Converter.deserialize(body.string(), clazz)))
                            } ?: run {
                                trySend(NetworkResult.Success<T>(Converter.deserialize("", clazz)))
                            }
                        } else {
                            when(response.code){
                                in 300..399 -> trySend(NetworkResult.Error(response.code, ErrorType.REDIRECT))
                                in 400..499 -> trySend(NetworkResult.Error(response.code, ErrorType.CREDENTIALS))
                                in 500..599 -> trySend(NetworkResult.Error(response.code, ErrorType.SERVER))
                                else              -> trySend(NetworkResult.Error(response.code, ErrorType.RANDOM))
                            }
                        }
                    }
                })
                LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call queued")
                awaitClose {
                    call.cancel()
                    LoggerProvider.getLogger()?.log(this::class.java.simpleName, "rest call finished")
                }
            }.flowOn(Scopes.getIODispatcher())
        }
    }
}
