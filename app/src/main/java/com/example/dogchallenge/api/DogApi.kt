package com.example.dogchallenge.api

import com.example.dogchallenge.api.models.Breed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {

    @GET("images/search")
    suspend fun getBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<List<Breed>>
}