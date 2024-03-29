package com.example.dogchallenge.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogchallenge.R
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.repository.BreedRepository
import com.example.dogchallenge.utils.ApiNotResponding
import com.example.dogchallenge.utils.AppResult
import com.example.dogchallenge.utils.NoInternetConnectionException
import com.example.dogchallenge.utils.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PAGE_SIZE = 20

class BreedListViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: BreedRepository
) : ViewModel() {
    var state by mutableStateOf<UiState?>(null)
        private set

    var breedList by mutableStateOf<List<Breed>>(ArrayList())
        private set

    var filteredBreedList by mutableStateOf<List<Breed>>(ArrayList())
        private set

    var ascendingOrder by mutableStateOf(true)
        private set

    var page by mutableStateOf(1)
        private set

    private var totalItems: Int = 0

    init {
        getBreeds(0)
    }

    fun getBreeds(pageToLoad: Int, resetList: Boolean = false) {
        state = UiState.Loading(true)
        val order = if (ascendingOrder) "asc" else "desc"
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    repository.getDogBreeds(
                        PAGE_SIZE,
                        pageToLoad,
                        order
                    )
                }

            state = UiState.Loading(false)
            when (result) {
                is AppResult.Success -> {
                    totalItems = result.successData.totalItems ?: -1
                    if (resetList) {
                        breedList = result.successData.breeds
                        page = 1
                    } else
                        appendBreeds(result.successData.breeds)

                    state = UiState.Error(null)
                }
                is AppResult.Error -> {
                    val error = when (result.exception) {
                        is ApiNotResponding ->
                            R.string.api_not_responding
                        is NoInternetConnectionException ->
                            R.string.no_network_connectivity
                        else ->
                            R.string.something_went_wrong
                    }
                    state = UiState.Error(error)
                }
                else -> state = UiState.Error(R.string.something_went_wrong)
            }
        }
    }

    fun changeSortingOrder() {
        ascendingOrder = !ascendingOrder
    }

    fun nextPage() {
        if (breedList.size < totalItems) {
            getBreeds(page)
            page += 1
        } else {
            state = UiState.Warning(R.string.no_more_data)
        }
    }

    fun searchBreed(breed: String) {
        state = UiState.Loading(true)
        viewModelScope.launch {
            val result = withContext(ioDispatcher) { repository.searchDogBreeds(breed) }

            state = UiState.Loading(false)
            when (result) {
                is AppResult.Success -> {
                    filteredBreedList = result.successData

                    state = UiState.Error(null)
                }
                is AppResult.Error -> {
                    val error = when (result.exception) {
                        is ApiNotResponding ->
                            R.string.api_not_responding
                        is NoInternetConnectionException ->
                            R.string.no_network_connectivity
                        else ->
                            R.string.something_went_wrong
                    }
                    state = UiState.Error(error)
                }
                else -> UiState.Error(R.string.something_went_wrong)
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