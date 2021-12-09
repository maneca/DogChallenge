package com.example.dogchallenge.di

import com.example.dogchallenge.api.DogApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    fun provideDogApi(retrofit: Retrofit): DogApi {
        return retrofit.create(DogApi::class.java)
    }

    single { provideDogApi(get()) }
}