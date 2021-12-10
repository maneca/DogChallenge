package com.example.dogchallenge.repository

import android.util.Log
import com.example.dogchallenge.api.BreedApi
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.db.dao.BreedDao
import com.example.dogchallenge.db.model.BreedDB
import com.example.dogchallenge.repository.models.BreedsInfo
import com.example.dogchallenge.utils.*

class BreedRepositoryImp(
    private val api: BreedApi,
    private val dao: BreedDao
) : BreedRepository {

    override suspend fun getDogBreeds(
        limit: Int,
        page: Int,
        order: String
    ): AppResult<BreedsInfo>? {
        return try {
            val response = api.getBreeds(limit, page, order)

            if (response.isSuccessful) {
                response.body()?.let {

                    val breedsDB = mutableListOf<BreedDB>()
                    it.forEach { breed ->
                        breedsDB.add(BreedDB(breed))
                    }
                    dao.addBreeds(breedsDB.toList())

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
        } catch (ex: NoInternetConnectionException) {
            val breedsInfo = getBreedsFromDatabase(order)
            if (breedsInfo != null) {
                AppResult.Success(breedsInfo)
            } else {
                ex.message?.let { Log.d("DogChallenge", it) }
                AppResult.Error(NoInternetConnectionException())
            }
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
        } catch (ex: NoInternetConnectionException) {
            val breeds = searchBreedsFromDatabase(query)
            AppResult.Success(breeds)

        } catch (e: Exception) {
            e.message?.let { Log.d("DogChallenge", it) }
            AppResult.Error(UnknownException())
        }
    }

    private fun getBreedsFromDatabase(order: String): BreedsInfo? {
        val breedsFromDB = dao.getBreeds(order == "asc")
        return if (breedsFromDB.isNotEmpty()) {
            val breeds = mutableListOf<Breed>()
            breedsFromDB.forEach { breedDB ->
                val b = Breed(breedDB = breedDB)
                breeds.add(b)
            }
            BreedsInfo(breeds.size, breeds)
        } else {
            null
        }
    }

    private fun searchBreedsFromDatabase(query: String): List<Breed> {
        val breedsFromDB = dao.searchBreeds(query)

        val breeds = mutableListOf<Breed>()
        breedsFromDB.forEach { breedDB ->
            val b = Breed(breedDB = breedDB)
            breeds.add(b)
        }
        return breeds
    }
}