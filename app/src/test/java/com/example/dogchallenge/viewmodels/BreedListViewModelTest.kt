package com.example.dogchallenge.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.repository.BreedRepository
import com.example.dogchallenge.repository.models.BreedsInfo
import com.example.dogchallenge.utils.AppResult
import com.example.dogchallenge.utils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BreedListViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: BreedRepository

    private lateinit var viewModel: BreedListViewModel

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

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `get all breeds, successful`() = runBlockingTest {

        assertNotNull(mockRepo)
        val breedInfo = BreedsInfo(1, listOf(breed))
        coEvery { mockRepo.getDogBreeds(any(), any(), any()) } coAnswers {
            AppResult.Success(
                breedInfo
            )
        }
        viewModel = BreedListViewModel(testDispatcher, mockRepo)

        assertEquals(1, viewModel.breedList.size)
        assertEquals(breed.name, viewModel.breedList[0].name)
    }

    @Test
    fun `get all breeds, throws error`() = runBlockingTest {
        assertNotNull(mockRepo)
        coEvery {
            mockRepo.getDogBreeds(
                any(),
                any(),
                any()
            )
        } coAnswers { AppResult.Error(Exception("")) }
        viewModel = BreedListViewModel(testDispatcher, mockRepo)

        assertNotNull(viewModel.state)
        assertEquals(0, viewModel.breedList.size)
    }

    @Test
    fun `get all breeds, reached end`() = runBlockingTest {

        assertNotNull(mockRepo)
        assertNotNull(mockRepo)
        val breedInfo = BreedsInfo(1, listOf(breed))
        coEvery { mockRepo.getDogBreeds(any(), any(), any()) } coAnswers {
            AppResult.Success(
                breedInfo
            )
        }
        viewModel = BreedListViewModel(testDispatcher, mockRepo)

        viewModel.nextPage()
        viewModel.nextPage()

        assertNotNull (viewModel.state)
    }

    @Test
    fun `search breed, successful`() = runBlockingTest {

        assertNotNull(mockRepo)
        coEvery { mockRepo.getDogBreeds(any(), any(), any()) } coAnswers {
            AppResult.Success(
                BreedsInfo(0, listOf())
            )
        }
        coEvery { mockRepo.searchDogBreeds(any()) } coAnswers { AppResult.Success(listOf(breed)) }
        viewModel = BreedListViewModel(testDispatcher, mockRepo)

        viewModel.searchBreed("b")

        assertEquals(1, viewModel.filteredBreedList.size)
        assertEquals(breed.name, viewModel.filteredBreedList[0].name)
    }

    @Test
    fun `search breed, throws error`() = runBlockingTest {
        assertNotNull(mockRepo)
        assertNotNull(mockRepo)
        coEvery { mockRepo.getDogBreeds(any(), any(), any()) } coAnswers {
            AppResult.Success(
                BreedsInfo(0, listOf())
            )
        }
        coEvery { mockRepo.searchDogBreeds(any()) } coAnswers { AppResult.Error(Exception("")) }
        viewModel = BreedListViewModel(testDispatcher, mockRepo)

        viewModel.searchBreed("b")

        assertNotNull(viewModel.state)
        assertEquals(0, viewModel.filteredBreedList.size)
    }
}