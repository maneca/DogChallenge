package com.example.dogchallenge.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.dogchallenge.R
import com.example.dogchallenge.api.BreedApi
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.db.dao.BreedDao
import com.example.dogchallenge.repository.models.BreedsInfo
import com.example.dogchallenge.utils.AppResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response


@RunWith(AndroidJUnit4::class)
@SmallTest
class BreedRepositoryTest {

    @MockK
    private lateinit var mockApi: BreedApi

    @MockK(relaxUnitFun = true)
    private lateinit var mockDao: BreedDao

    private lateinit var context: Context

    private lateinit var repository: BreedRepository

    private val breed = Breed(
        "Small rodent hunting, lapdog",
        "Toy",
        null,
        1,
        null,
        "10 - 12 years",
        "Affenpinscher",
        "Germany, France",
        "BJa4kxc4X",
        "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
        null
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        context = ApplicationProvider.getApplicationContext()
        repository = BreedRepositoryImp(mockApi, mockDao)
    }

    @Test
    fun getBreedsFromNetwork() = runBlocking {
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)

        val response = Response.success(
            listOf(breed),
            okhttp3.Response.Builder()
                .code(200)
                .message("Response.success()")
                .addHeader("pagination-count", "1")
                .protocol(Protocol.HTTP_2)
                .request(
                    Request.Builder().url(context.resources.getString(R.string.BASE_URL)).build()
                )
                .receivedResponseAtMillis(1619053449513)
                .sentRequestAtMillis(1619053443814)
                .build()
        )

        coEvery { mockApi.getBreeds(10, 0, "asc") } coAnswers { response }

        val result =
            repository.getDogBreeds(10, 0, "asc") as AppResult.Success<BreedsInfo>

        Assert.assertEquals(1, result.successData.totalItems)
        Assert.assertEquals(breed.name, result.successData.breeds[0].name)
    }

    @Test
    fun searchDogBreedsFromNetwork() = runBlocking {
        Assert.assertNotNull(mockApi)

        coEvery { mockApi.searchBreed("b") } coAnswers { Response.success(listOf(breed)) }

        val result =
            repository.searchDogBreeds("b") as AppResult.Success<List<Breed>>?

        Assert.assertEquals(1, result!!.successData.size)
        Assert.assertEquals(breed.name, result.successData[0].name)
    }
}