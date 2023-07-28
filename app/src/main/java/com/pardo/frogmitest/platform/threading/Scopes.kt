package com.pardo.frogmitest.platform.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

class Scopes {
    companion object {
        private lateinit var daoScopes : CoroutineScope
        private lateinit var ioDispatcher: CoroutineDispatcher
        private var job : Job = SupervisorJob()

        private lateinit var roomScopes : CoroutineScope
        private lateinit var roomDispatcher: CoroutineDispatcher
        private var roomJob : Job = SupervisorJob()

        fun getIODispatcher() : CoroutineDispatcher {
            if(!this::ioDispatcher.isInitialized){
                ioDispatcher = Executors.getIOExecutor().asCoroutineDispatcher()
            }
            return ioDispatcher
        }

        fun getIOScope() : CoroutineScope {
            if(!this::daoScopes.isInitialized){
                daoScopes = CoroutineScope(getIODispatcher() + job)
            }
            return daoScopes
        }

        fun getRoomDispatcher() : CoroutineDispatcher {
            if(!this::roomDispatcher.isInitialized){
                roomDispatcher = Executors.getRoomExecutor().asCoroutineDispatcher()
            }
            return roomDispatcher
        }

        fun getRoomScope() : CoroutineScope {
            if(!this::roomScopes.isInitialized){
                roomScopes = CoroutineScope(getRoomDispatcher() + roomJob)
            }
            return roomScopes
        }

        fun launch(block: suspend CoroutineScope.() -> Unit) : Job {
            return getIOScope().launch {
                block()
            }
        }

        fun end(){
            job.cancel()
            roomJob.cancel()
        }

    }
}
