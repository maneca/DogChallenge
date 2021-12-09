package com.example.dogchallenge

import android.app.Application
import com.example.dogchallenge.di.apiModule
import com.example.dogchallenge.di.networkModule
import com.example.dogchallenge.di.repositoryModule
import com.example.dogchallenge.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DogApplication)
            modules(
                listOf(
                    apiModule,
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}