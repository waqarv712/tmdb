package com.waqar.tmdb

import android.app.Application
import android.os.StrictMode
import com.facebook.stetho.Stetho
import com.waqar.tmdb.utils.AppExecutor
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TMDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppExecutor.executorService!!.submit {
            if (BuildConfig.DEBUG) {
                Timber.d("******* Timber Debug Tree Planted ********")
                StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork() // or .detectAll() for all detectable problems
                        .penaltyLog()
                        .build()
                )
                StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build()
                )
//                    .detectLeakedClosableObjects()

            }
        }

        Stetho.initializeWithDefaults(this)

    }

}