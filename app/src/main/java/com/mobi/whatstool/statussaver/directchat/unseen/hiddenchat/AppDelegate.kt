package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat


import androidx.multidex.MultiDexApplication
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class KoinApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KoinApp)
            modules(appModule)
        }
    }
}