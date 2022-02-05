package com.waqar.tmdb.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutor {
    private var sInstance: ExecutorService? = null
    val executorService: ExecutorService?
        get() {
            if (sInstance == null) {
//            sInstance = Executors.newSingleThreadExecutor();
                sInstance = Executors.newFixedThreadPool(3)
            }
            return sInstance
        }
}