package com.pardo.frogmitest.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.domain.repositories.StoresRepository
import com.pardo.frogmitest.platform.threading.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoresViewModel  : ViewModel() {

    private var storesFlow : MutableStateFlow<List<StoreCellData>?> = MutableStateFlow(null)
    val stores : StateFlow<List<StoreCellData>?> = storesFlow

    fun getStores() : Job {
        return Scopes.getIOScope().launch {
            StoresRepository.getInstance().getStores().collect{
                storesFlow.value = it
            }
        }
    }
}