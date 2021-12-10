package com.example.dogchallenge.di

import com.example.dogchallenge.api.BreedApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    fun provideDogApi(retrofit: Retrofit): BreedApi {
        return retrofit.create(BreedApi::class.java)
    }

    single { provideDogApi(get()) }
}