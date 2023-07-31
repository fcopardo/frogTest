package com.pardo.frogmitest.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.domain.models.ui.ViewModelResult
import com.pardo.frogmitest.domain.models.ui.ViewModelValue
import com.pardo.frogmitest.domain.repositories.StoresRepository
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoresViewModel  : ViewModel() {

    var storesFlow : MutableStateFlow<ViewModelResult<MutableList<StoreCellData>>> = MutableStateFlow(
        ViewModelResult.Success(mutableListOf())
    )
    var storeState by mutableStateOf(ViewModelValue<MutableList<StoreCellData>>(mutableListOf(), true, ""))

    var currentPage by mutableStateOf(0)
    var lastPage by mutableStateOf(10)
    var loadedPages by mutableStateOf(mutableMapOf<Int, Int>())

    //Functional but unused. Just for playing around.
    fun getStores() : Job {
        return Scopes.getIOScope().launch {
            StoresRepository.getInstance().getStores().collect{
                lateinit var result : ViewModelResult<MutableList<StoreCellData>>
                if(it.isSuccessful()) {
                    var safeData : MutableList<StoreCellData> = if (it.getValue() != null) {
                        it.getValue()!!
                    } else {
                        mutableListOf()
                    }
                    result = ViewModelResult.Success(safeData)
                }
                 else {
                     result = ViewModelResult.Error(it.getMessage())
                }
                storesFlow.value = result
            }
        }
    }

    fun getStoresPage(page : Int = getNextPageNumber()) {
        viewModelScope.launch {
            if(!loadedPages.containsKey(page) || (loadedPages.containsKey(page) && loadedPages[page] == 0) ){
                StoresRepository.getInstance().getStores(page).collect {
                    if(it.isSuccessful()){
                        it.getValue()?.let { paginatedData->
                            paginatedData.getData()?.let { newResults ->
                                var newList = mutableListOf<StoreCellData>().apply {
                                    addAll(storeState.data)
                                    addAll(newResults)
                                }
                                loadedPages[page] = newList.size
                                storeState = ViewModelValue(newList, true, "")
                            }
                            currentPage = paginatedData.getCurrentPage()
                            lastPage = paginatedData.getLastPage()
                        }
                    } else {
                        storeState = ViewModelValue(mutableListOf(), false, it.getMessage())
                    }
                }
            } else {
                LoggerProvider.getLogger()?.log("repo", "page already added")
            }
        }
    }

    fun getCurrentPageNumber() : Int {
        return currentPage
    }

    fun getLastPageNumber() : Int {
        return lastPage
    }

    fun getNextPageNumber() : Int {
        currentPage++
        if(currentPage>lastPage){
            return lastPage
        }
        return currentPage
    }

    fun arePagesLeft() : Boolean {
        return currentPage+1<lastPage
    }

    override fun onCleared(){
        lastPage = 10
        currentPage = 1
        super.onCleared()
    }
}