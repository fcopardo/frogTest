package com.pardo.frogmitest.domain.repositories

import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.domain.models.network.StoresResponse
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

    fun getStores() = flow<List<StoreCellData>> {
        LoggerProvider.getLogger()?.log("Frogmi", "repository working")
        RestClient.getData(API, mutableMapOf(), StoresResponse::class).collect { result->
            LoggerProvider.getLogger()?.log("Frogmi", "repository collecting")
            if(result is RestClient.NetworkResult.Success){
                var data = result.value as StoresResponse
                data?.let { response ->
                    var stores = mutableListOf<StoreCellData>()
                    response.data?.forEach { datum->
                        datum.attributes?.let {
                            stores.add(Converter.jsonToStoreCellData(it))
                        }
                    }
                    emit(stores)
                }
            } else {
                //TODO: Remove this after finishing v1
                LoggerProvider.getLogger()?.log("Frogmi", "repository mocking")
                var cellA = StoreCellData("Store 1", "STCT000000", "Presidente Errazuriz 1421, Santiago, Chile")
                var cellB = StoreCellData("Store 2", "STCT000001", "Moneda 1022, Santiago, Chile")
                var cellC = StoreCellData("Store 3", "STCT000003", "Moneda 1022, Santiago, Chile")
                var cellD = StoreCellData("Store 4", "STCT000004", "Escandinavia 152, Santiago, Chile")
                var cellE = StoreCellData("Store 4", "STCT000005", "Portugal 3529, Santiago, Chile")
                var cellF = StoreCellData("Store 4", "STCT000006", "Bilbao 7777, Santiago, Chile")
                var myList = mutableListOf<StoreCellData>().apply {
                    add(cellA)
                    add(cellB)
                    add(cellC)
                    add(cellD)
                    add(cellE)
                    add(cellF)
                }
                emit(myList)
            }
        }
    }.flowOn(Scopes.getIODispatcher())

}