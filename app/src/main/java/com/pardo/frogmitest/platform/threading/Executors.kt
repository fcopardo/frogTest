package com.pardo.frogmitest.platform.threading

import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Allows running lambdas in the background with regular java APIs.
 */
class Executors {
    companion object {

        private lateinit var iOExecutor : ThreadPoolExecutor
        private lateinit var quickExecutor : ThreadPoolExecutor

        /**
         * Arbitrary values. Most high performance clusters range between three to one core.
         */
        var cores = 2
        var cluster = Runtime.getRuntime().availableProcessors()
        var highPowerCluster = cluster+1

        /**
         * One common problem with android development is balancing performance and power consumption.
         * The android Os will try to use as little power as possible, so CPUs are divided into
         * specialized clusters, with a different amount of cores, and different clock ranges per
         * each cluster. Normally, the governor will raise frequencies and wake up dormant cores,
         * then dormant clusters, when the active cluster passes a certain threshold of activity.
         * So, your pools need to raise the probability of hitting those limits, without risking
         * taking over the entire CPU for a single type of task (like, slow IO operations), to avoid
         * slowing down the app, and the entire phone.
         */
        fun getIOExecutor() : ThreadPoolExecutor {
            if(!this::iOExecutor.isInitialized){
                val poolSize = if(isDeviceStressed()){
                    highPowerCluster*2
                } else {
                    highPowerCluster*2
                }
                iOExecutor = ThreadPoolExecutor(cores, poolSize, 5000, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
            }
            return iOExecutor
        }

        fun getQuickExecutor() : ThreadPoolExecutor {
            if(!this::quickExecutor.isInitialized){
                val poolSize = if(isDeviceStressed()){
                    cluster+1
                } else {
                    cluster*2
                }
                quickExecutor = ThreadPoolExecutor(cluster, poolSize, 5000, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
            }
            return quickExecutor
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

        private fun isDeviceStressed(): Boolean {
            var usedMemoryPercentage = (Runtime.getRuntime().freeMemory()*100)/Runtime.getRuntime().totalMemory()
            return usedMemoryPercentage<30
        }
    }
}