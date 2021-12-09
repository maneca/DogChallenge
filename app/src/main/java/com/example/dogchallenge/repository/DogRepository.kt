package com.example.dogchallenge.repository

import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.utils.AppResult

interface DogRepository {

    suspend fun getDogBreeds(limit: Int, page: Int): AppResult<List<Breed>>?
}