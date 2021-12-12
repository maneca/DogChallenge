package com.example.dogchallenge.di

import com.example.dogchallenge.viewmodels.BreedListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BreedListViewModel(Dispatchers.IO, get()) }
}