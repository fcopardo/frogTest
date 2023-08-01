package com.pardo.frogmitest.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pardo.frogmitest.domain.models.domain.PaginatedData
import com.pardo.frogmitest.domain.models.domain.RepositoryResult
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.domain.models.ui.ViewModelResult
import com.pardo.frogmitest.domain.models.ui.ViewModelValue
import com.pardo.frogmitest.domain.repositories.StoresRepository
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StoresViewModel  : ViewModel() {

    /**
     * A viewmodel is linked to the UI, os it must run in the UI thread. In the coroutine world,
     * that means Dispatchers.Main, but that construct does not exist outside android, so
     * we encapsulate the view level dispatcher so we can replace it later.
     */
    interface ScopeProvider {
        fun getScope () : CoroutineScope
    }

    var storeState by mutableStateOf(ViewModelValue<MutableList<StoreCellData>>(mutableListOf(), true, ""))

    var currentPage by mutableStateOf(0)
    var lastPage by mutableStateOf(10)

    private var loadedPages by mutableStateOf(mutableMapOf<Int, Int>())

    var scopeProvider : ScopeProvider = object : ScopeProvider {
        override fun getScope(): CoroutineScope {
            return viewModelScope
        }
    }

    var clickListener : (item : StoreCellData)-> Unit = {}

    fun getStoresPage(page : Int = getNextPageNumber()) {
        scopeProvider.getScope().launch {
            if(!loadedPages.containsKey(page) || (loadedPages.containsKey(page) && loadedPages[page] == 0) ){
                StoresRepository.getInstance().getStores(page).collect {
                    setState(it, page)
                }
            } else {
                LoggerProvider.getLogger()?.log("repo", "page already added")
            }
        }
    }

    /**
     * Setting the entire state through an exposed method is needed in order to test the process.
     * While we can fire the previous function, we won't be able to halt execution and wait for the results.
     */
    fun setState(repoData : RepositoryResult<PaginatedData<List<StoreCellData>>>, page: Int = getCurrentPageNumber()) {
        if(repoData.isSuccessful()){
            repoData.getValue()?.let { paginatedData->
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
            storeState = ViewModelValue(mutableListOf(), false, repoData.getMessage())
        }
    }

    fun getCurrentPageNumber() : Int {
        return currentPage
    }

    fun getNextPageNumber() : Int {
        currentPage++
        if(currentPage>lastPage){
            return lastPage
        }
        return currentPage
    }

    override fun onCleared(){
        lastPage = 10
        currentPage = 1
        super.onCleared()
    }
}