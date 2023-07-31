package com.pardo.frogmitest.ui

import com.pardo.frogmitest.BuildConfig
import com.pardo.frogmitest.JVMRestPlatformDependencies
import com.pardo.frogmitest.LoggerJVM
import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.data.secure.CredentialsProvider
import com.pardo.frogmitest.domain.repositories.StoresRepository
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.platform.threading.Scopes
import com.pardo.frogmitest.ui.viewmodels.StoresViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Tests the page loading process. State observation is a system process, so it does not need
 * testing within this scenario; nonetheless, the entire scenario could be replicated at this level.
 */
class StoresViewModelTest {
    private lateinit var model : StoresViewModel

    init {
        LoggerProvider.setLogger(LoggerJVM())
        RestClient.setPlatformDependencies(JVMRestPlatformDependencies())
        CredentialsProvider.getInstance().setToken(BuildConfig.AUTH)
        CredentialsProvider.getInstance().setCompanyId(BuildConfig.COMPANY_ID)
    }

    @Before
    fun setUp(){
        model = StoresViewModel()
        model.scopeProvider = object : StoresViewModel.ScopeProvider {
            override fun getScope(): CoroutineScope {
                return Scopes.getIOScope()
            }
        }
        model.getStoresPage()
    }

    @Test
    fun testPageLoading() = runTest {
        var amount = 0
        StoresRepository.getInstance().getStores(model.getNextPageNumber()).take(1).collect {
            model.setState(it)
            println(model.storeState.data.size)
            it.getValue()?.let { data->
                assert(model.storeState.data.isNotEmpty())
                data.getData()?.let { safeData->
                    assert(amount<model.storeState.data.size)
                    amount += model.storeState.data.size
                }
            } ?: run {
                assert(model.storeState.data.isEmpty())
            }
        }
        StoresRepository.getInstance().getStores(model.getNextPageNumber()).take(1).collect {
            model.setState(it)
            println(model.storeState.data.size)
            it.getValue()?.let { data->
                assert(model.storeState.data.isNotEmpty())
                data.getData()?.let { safeData->
                    assert(amount<model.storeState.data.size)
                    amount += model.storeState.data.size
                }
            } ?: run {
                assert(model.storeState.data.isEmpty())
            }
        }
        println(amount)

    }

}