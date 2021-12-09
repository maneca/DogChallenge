package com.example.dogchallenge.di

import com.example.dogchallenge.api.DogApi
import com.example.dogchallenge.repository.DogRepository
import com.example.dogchallenge.repository.DogRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    fun provideDogRepository(api: DogApi): DogRepository {
        return DogRepositoryImp(api)
    }

    single { provideDogRepository(get()) }
}