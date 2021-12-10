package com.example.dogchallenge.repository

import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.repository.models.BreedsInfo
import com.example.dogchallenge.utils.AppResult

interface BreedRepository {

    suspend fun getDogBreeds(limit: Int, page: Int, order: String): AppResult<BreedsInfo>?

    suspend fun searchDogBreeds(query: String): AppResult<List<Breed>>?
}