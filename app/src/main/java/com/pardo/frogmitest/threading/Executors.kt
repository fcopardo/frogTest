package com.pardo.frogmitest.threading

import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class Executors {
    companion object {

        private lateinit var iOExecutor : ThreadPoolExecutor
        private lateinit var quickExecutor : ThreadPoolExecutor
        private lateinit var roomExecutor : ThreadPoolExecutor

        var cores = 8
        var cluster = 2

        fun getIOExecutor() : ThreadPoolExecutor {
            if(!this::iOExecutor.isInitialized){
                iOExecutor = ThreadPoolExecutor(cores, cores *5, 5000, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
            }
            return iOExecutor
        }

        fun getQuickExecutor() : ThreadPoolExecutor {
            if(!this::quickExecutor.isInitialized){
                quickExecutor = ThreadPoolExecutor(cluster, cluster *2, 5000, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
            }
            return quickExecutor
        }

        fun getRoomExecutor() : ThreadPoolExecutor {
            if(!this::roomExecutor.isInitialized){
                roomExecutor = ThreadPoolExecutor(cluster, cluster +2, 8000, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
            }
            return roomExecutor
        }

        private fun runTask(task:() -> Unit, executor: ThreadPoolExecutor): Future<*>? {
            return executor.submit{ task() }
        }

        fun runQuickTask(task: () -> Unit): Future<*>? {
            return runTask(task, getQuickExecutor())
        }

        fun runIOTask(task: () -> Unit): Future<*>? {
            return runTask(task, getIOExecutor())
        }

        fun runRoomTask(task: () -> Unit): Future<*>? {
            return runTask(task, getRoomExecutor())
        }
    }
}