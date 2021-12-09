package com.example.dogchallenge.di

import com.example.dogchallenge.viewmodels.BreedListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BreedListViewModel(get()) }
}