package com.example.dogchallenge.repository

import android.util.Log
import com.example.dogchallenge.api.DogApi
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.repository.models.BreedsInfo
import com.example.dogchallenge.utils.*

class DogRepositoryImp(private val api: DogApi) : DogRepository {

    override suspend fun getDogBreeds(limit: Int, page: Int): AppResult<BreedsInfo>? {
        return try {
            val response = api.getBreeds(limit, page)

            if (response.isSuccessful) {
                response.body()?.let {

                    val breedsInfo = BreedsInfo(
                        response.headers()["pagination-count"]!!.toInt(),
                        it
                    )
                    AppResult.Success(breedsInfo)
                }
            } else {
                Log.d("DogChallenge", ApiErrorUtils.parseError(response).message)
                AppResult.Error(ApiNotResponding())
            }
        } catch (ex: NoConnectivityException) {
            ex.message?.let { Log.d("DogChallenge", it) }
            AppResult.Error(NoConnectivityException())
        } catch (e: Exception) {
            e.message?.let { Log.d("DogChallenge", it) }
            AppResult.Error(UnknownException())
        }
    }

    override suspend fun searchDogBreeds(query: String): AppResult<List<Breed>>? {
        return try {
            val response = api.searchBreed(query)

            if (response.isSuccessful) {
                response.body()?.let {
                    AppResult.Success(it)
                }
            } else {
                Log.d("DogChallenge", ApiErrorUtils.parseError(response).message)
                AppResult.Error(ApiNotResponding())
            }
        } catch (ex: NoConnectivityException) {
            ex.message?.let { Log.d("DogChallenge", it) }
            AppResult.Error(NoConnectivityException())
        } catch (e: Exception) {
            e.message?.let { Log.d("DogChallenge", it) }
            AppResult.Error(UnknownException())
        }
    }
}