package com.example.dogchallenge.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogchallenge.R
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.repository.DogRepository
import com.example.dogchallenge.utils.ApiNotResponding
import com.example.dogchallenge.utils.AppResult
import com.example.dogchallenge.utils.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PAGE_SIZE = 20

class BreedListViewModel(
    private val repository: DogRepository
) : ViewModel() {
    var showLoading by mutableStateOf(false)
        private set

    var breedList by mutableStateOf<List<Breed>>(ArrayList())
        private set

    var filteredBreedList by mutableStateOf<List<Breed>>(ArrayList())
        private set

    var showError by mutableStateOf<Int?>(null)
        private set

    var showWarning by mutableStateOf<Int?>(null)
        private set

    var page by mutableStateOf(1)
        private set

    private var totalItems: Int = 0

    init {
        getBreeds(0)
    }

    private fun getBreeds(pageToLoad: Int) {
        showLoading = true
        viewModelScope.launch {
            val result =
                withContext(Dispatchers.IO) { repository.getDogBreeds(PAGE_SIZE, pageToLoad) }

            showLoading = false
            when (result) {
                is AppResult.Success -> {
                    totalItems = result.successData.totalItems
                    appendBreeds(result.successData.breeds)

                    showError = null
                }
                is AppResult.Error -> {
                    showError = when (result.exception) {
                        is ApiNotResponding ->
                            R.string.api_not_responding
                        is NoConnectivityException ->
                            R.string.no_network_connectivity
                        else ->
                            R.string.something_went_wrong
                    }
                }
                else -> showError = R.string.something_went_wrong
            }
        }
    }

    fun nextPage() {
        if (breedList.size < totalItems) {
            getBreeds(page)
            page += 1
        } else {
            showWarning = R.string.no_more_data
        }

    }

    fun searchBreed(breed: String) {
        showLoading = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.searchDogBreeds(breed) }

            showLoading = false
            when (result) {
                is AppResult.Success -> {
                    filteredBreedList = result.successData

                    showError = null
                }
                is AppResult.Error -> {
                    showError = when (result.exception) {
                        is ApiNotResponding ->
                            R.string.api_not_responding
                        is NoConnectivityException ->
                            R.string.no_network_connectivity
                        else ->
                            R.string.something_went_wrong
                    }
                }
                else -> showError = R.string.something_went_wrong
            }
        }
    }

    // Append challenges to the current list
    private fun appendBreeds(breeds: List<Breed>) {
        val current = ArrayList(breedList)
        current.addAll(breeds)
        breedList = current
    }

}