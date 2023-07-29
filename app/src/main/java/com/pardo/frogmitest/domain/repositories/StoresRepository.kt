package com.pardo.frogmitest.domain.repositories

import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.data.secure.CredentialsProvider
import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.domain.models.network.StoresResponse
import com.pardo.frogmitest.domain.models.domain.RepositoryResult
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StoresRepository private constructor() {

    companion object {
        val API = "https://api.frogmi.com/api/v3/stores"
        val HEADER_COMPANY = "X-Company-Uuid"
        val HEADER_AUTH = "Authentication"

        private var instance = StoresRepository()

        fun getInstance() : StoresRepository{
            return instance
        }
    }

    fun getStores() = flow<RepositoryResult<List<StoreCellData>>> {
        LoggerProvider.getLogger()?.log("Frogmi", "repository working")
        var headers = mutableMapOf<String, String>()
        headers["Authorization"] = CredentialsProvider.getInstance().getToken()
        headers["X-Company-UUID"] = CredentialsProvider.getInstance().getCompanyId()
        headers["Content-Type"] = "application/vnd.api+json"
        RestClient.getData(API, headers, StoresResponse::class).collect { result->
            LoggerProvider.getLogger()?.log("Frogmi", "repository collecting")
            lateinit var repoResult : RepositoryResult<List<StoreCellData>>
            if(result is RestClient.NetworkResult.Success){
                LoggerProvider.getLogger()?.log("Frogmi", "repository received data")
                var data = result.value as StoresResponse
                data?.let { response ->
                    var stores = mutableListOf<StoreCellData>()
                    response.data?.forEach { datum->
                        datum.attributes?.let {
                            stores.add(Converter.jsonToStoreCellData(it))
                        }
                    }
                    repoResult = RepositoryResult(true, "", stores)
                }
            } else {
                val failure = result as RestClient.NetworkResult.Error
                LoggerProvider.getLogger()?.log("Frogmi", "repository collection failed, code: ${failure.code}, type: ${failure.code}")

                val message = when(failure.type){
                    RestClient.ErrorType.CREDENTIALS -> "Authentication error. Please login again."
                    RestClient.ErrorType.CONNECTION  -> "The server could not be reached due to a connectivity error." +
                            "Please check your connection."
                    RestClient.ErrorType.SERVER      -> "The server is not available. Please reach out to our support channels."
                    RestClient.ErrorType.REDIRECT    -> "We can't reach the server right now due to an excess of redirections. " +
                            "Try with a different network connection, or remove any VPNs or proxies in use."
                    else                             -> "We can't process your request right now. Please close the app and try later."
                }
                repoResult = RepositoryResult(false, message)
            }
            emit(repoResult)
        }
    }.flowOn(Scopes.getIODispatcher())

}