package com.example.dogchallenge.di

import com.example.dogchallenge.api.BreedApi
import com.example.dogchallenge.db.dao.BreedDao
import com.example.dogchallenge.repository.BreedRepository
import com.example.dogchallenge.repository.BreedRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
    fun provideDogRepository(api: BreedApi, dao: BreedDao): BreedRepository {
        return BreedRepositoryImp(api, dao)
    }

    single { provideDogRepository(get(), get()) }
}