package com.pardo.frogmitest.platform.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

/**
 * Transforms Executors into Coroutine scopes, to reduce the chance of filling the CPU with
 * slows tasks when using Dispatchers.IO. Also, makes operation run in them more predictable
 * outside android devices.
 */
class Scopes {
    companion object {
        private lateinit var ioScopes : CoroutineScope
        private lateinit var ioDispatcher: CoroutineDispatcher
        private var job : Job = SupervisorJob()

        fun getIODispatcher() : CoroutineDispatcher {
            if(!this::ioDispatcher.isInitialized){
                ioDispatcher = Executors.getIOExecutor().asCoroutineDispatcher()
            }
            return ioDispatcher
        }

        fun getIOScope() : CoroutineScope {
            if(!this::ioScopes.isInitialized){
                ioScopes = CoroutineScope(getIODispatcher() + job)
            }
            return ioScopes
        }

        fun launch(block: suspend CoroutineScope.() -> Unit) : Job {
            return getIOScope().launch {
                block()
            }
        }

        fun end(){
            job.cancel()
        }

    }
}
