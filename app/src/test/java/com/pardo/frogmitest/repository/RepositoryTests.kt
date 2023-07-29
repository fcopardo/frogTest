package com.pardo.frogmitest.repository

import com.pardo.frogmitest.BuildConfig
import com.pardo.frogmitest.JVMRestPlatformDependencies
import com.pardo.frogmitest.LoggerJVM
import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.data.secure.CredentialsProvider
import com.pardo.frogmitest.domain.repositories.StoresRepository
import com.pardo.frogmitest.platform.LoggerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RepositoryTests {
    init {
        LoggerProvider.setLogger(LoggerJVM())
        RestClient.setPlatformDependencies(JVMRestPlatformDependencies())
        CredentialsProvider.getInstance().setToken(BuildConfig.AUTH)
        CredentialsProvider.getInstance().setCompanyId(BuildConfig.COMPANY_ID)
    }

    //This test requires providing a token in order to be able to authenticate.
    // Environment variables can not be read from junit, so read from a generated class instead or
    // directly provide the values.
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testRepo(){
        LoggerProvider.getLogger()?.log("testRepo", "Using token:${CredentialsProvider.getInstance().getToken()} and ID:${CredentialsProvider.getInstance().getCompanyId()}")
        runTest {
            StoresRepository.getInstance().getStores().take(1).collect {
                LoggerProvider.getLogger()?.log("testRepo result message", it.getMessage())
                if(CredentialsProvider.getInstance().getToken()!="") {
                    assert(it.isSuccessful())
                } else {
                    assert(!it.isSuccessful())
                }
            }
        }
    }
}