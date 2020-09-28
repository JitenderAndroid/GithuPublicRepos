package com.example.usergithubrepos

import android.app.Application
import com.example.usergithubrepos.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main Application class.
 * Dependency Injection initiated for all sub modules.
 */
open class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initiateKoin()
    }

    private fun initiateKoin() {
        startKoin{
            androidContext(this@MainApp)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}