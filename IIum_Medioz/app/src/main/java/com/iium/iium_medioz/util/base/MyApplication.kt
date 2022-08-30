package com.iium.iium_medioz.util.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.iium.iium_medioz.di.module
import com.iium.iium_medioz.util.common.CommonData
import com.iium.iium_medioz.util.pixel.PixelRatio
import com.iium.iium_medioz.util.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import javax.inject.Inject

class MyApplication : Application() {

    companion object {
        lateinit var prefs: PreferenceManager
        private var isMainNoticeViewed = false
        lateinit var pixelRatio: PixelRatio

        lateinit var databaseReference: DatabaseReference

        @Synchronized
        fun isIsMainNoticeViewed(): Boolean {
            return isMainNoticeViewed
        }

        @Synchronized
        fun setIsMainNoticeViewed(viewed: Boolean) {
           isMainNoticeViewed = viewed
        }

    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("test-data")
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        prefs = PreferenceManager(applicationContext)
        val commonData: CommonData = CommonData().getInstance()
        commonData.numStarted = 0

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                if (commonData.numStarted == 0) {
                    commonData.isMainRefresh = true
                    commonData.isForeground = true
                }
                commonData.numStarted++
            }
            override fun onActivityResumed(activity: Activity) {
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                commonData.numStarted--
                if (commonData.numStarted == 0) {
                    commonData.isForeground = false
                }
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        databaseReference = FirebaseDatabase.getInstance().reference

        startKoin {
            androidContext(this@MyApplication)
            modules(module)
        }
    }



}