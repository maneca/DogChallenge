package com.example.dogchallenge.di

import android.app.Application
import androidx.room.Room
import com.example.dogchallenge.db.BreedDatabase
import com.example.dogchallenge.db.dao.BreedDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): BreedDatabase {

        return Room.databaseBuilder(application, BreedDatabase::class.java, "breeds")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideBreedDao(database: BreedDatabase): BreedDao {
        return database.breedDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideBreedDao(get()) }
}