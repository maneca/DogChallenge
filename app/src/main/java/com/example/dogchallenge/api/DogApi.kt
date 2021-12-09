package com.example.dogchallenge.api

import com.example.dogchallenge.api.models.Breed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {

    @GET("breeds")
    suspend fun getBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("order") order: String = "asc"
    ): Response<List<Breed>>

    @GET("breeds/search")
    suspend fun searchBreed(@Query("q") query: String): Response<List<Breed>>
}