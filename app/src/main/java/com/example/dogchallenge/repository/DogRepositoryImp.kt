package com.example.dogchallenge.repository

import android.util.Log
import com.example.dogchallenge.api.DogApi
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.utils.*

class DogRepositoryImp(private val api: DogApi) : DogRepository {

    override suspend fun getDogBreeds(limit: Int, page: Int): AppResult<List<Breed>>? {
        return try {
            val response = api.getBreeds(limit, page)

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