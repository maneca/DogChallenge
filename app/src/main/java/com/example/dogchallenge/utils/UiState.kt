package com.example.dogchallenge.utils

sealed class UiState {
    class Loading(val show: Boolean): UiState()

    class Error(val message: Int?) : UiState()

    class Warning(val message: Int?) : UiState()
}
