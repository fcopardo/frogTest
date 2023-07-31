package com.pardo.frogmitest.domain.repositories

import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.data.secure.CredentialsProvider
import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.domain.models.domain.PaginatedData
import com.pardo.frogmitest.domain.models.network.StoresResponse
import com.pardo.frogmitest.domain.models.domain.RepositoryResult
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StoresRepository private constructor() {

    companion object {
        val API = "https://api.frogmi.com/api/v3/stores"

        private var instance = StoresRepository()

        fun getInstance() : StoresRepository {
            return instance
        }
    }

    suspend fun getStores() = flow<RepositoryResult<MutableList<StoreCellData>>> {
        RestClient.getData(API, getHeaders(), StoresResponse::class).collect { result->
            lateinit var repoResult : RepositoryResult<MutableList<StoreCellData>>
            if(result is RestClient.NetworkResult.Success){
                var data = result.value as StoresResponse
                data?.let { response ->
                    var stores = mutableListOf<StoreCellData>()
                    response.data?.forEach { datum->
                        datum.attributes?.let {
                            stores.add(Converter.jsonToStoreCellData(it, datum.id))
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

    suspend fun getStores(page : Int): Flow<RepositoryResult<PaginatedData<List<StoreCellData>>>> {
        LoggerProvider.getLogger()?.log("Frogmi", "repository working")
        return flow<RepositoryResult<PaginatedData<List<StoreCellData>>>> {
            var url = createUrl(page)
            LoggerProvider.getLogger()?.log("StoresRepo", "url to fetch is $url")
            RestClient.getData(url, getHeaders(), StoresResponse::class).collect { result->
                lateinit var repoResult : RepositoryResult<PaginatedData<List<StoreCellData>>>
                if(result is RestClient.NetworkResult.Success){
                    LoggerProvider.getLogger()?.log("Frogmi", "repository received data")
                    var data = result.value as StoresResponse
                    data?.let { response ->
                        var stores = mutableListOf<StoreCellData>()
                        var paginatedData = PaginatedData<List<StoreCellData>>()
                        response.links?.let { links->
                            links.self?.let{ self->
                                Converter.pageFromLinks(self)?.let { myPage->
                                    paginatedData.setCurrentPage(myPage)
                                } ?: run {
                                    paginatedData.setCurrentPage(page)
                                }
                            }
                            links.last?.let { last->
                                paginatedData.setLastPage(Converter.pageFromLinks(last)!!)
                            }
                        }
                        response.data?.forEach { datum->
                            datum.attributes?.let {
                                stores.add(Converter.jsonToStoreCellData(it, datum.id))
                            }
                        }
                        paginatedData.setData(stores)
                        repoResult = RepositoryResult(true, "", paginatedData)
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

    fun getHeaders(): MutableMap<String, String> {
        var headers = mutableMapOf<String, String>()
        headers["Authorization"] = CredentialsProvider.getInstance().getToken()
        headers["X-Company-UUID"] = CredentialsProvider.getInstance().getCompanyId()
        headers["Content-Type"] = "application/vnd.api+json"
        return headers
    }



    private fun createUrl(page : Int): String {
        var safePage = if(page < 0){
            1
        } else {
            page
        }
        return "$API?per_page=10&page=$safePage"
    }

}