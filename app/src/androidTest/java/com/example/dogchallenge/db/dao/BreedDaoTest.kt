package com.example.dogchallenge.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.dogchallenge.db.BreedDatabase
import com.example.dogchallenge.db.model.BreedDB
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BreedDaoTest {
    private lateinit var database: BreedDatabase
    private lateinit var dao: BreedDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BreedDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.breedDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getBreeds() {
        val breedA = BreedDB(
            name = "a",
            id = 1,
            category = "catA",
            origin = null,
            temperament = "temperamentA"
        )
        val breedB = BreedDB(
            name = "b",
            id = 2,
            category = "catB",
            origin = null,
            temperament = "temperamentB"
        )

        dao.addBreeds(listOf(breedA, breedB))

        var breeds = dao.getBreeds(true)
        Assert.assertEquals(2, breeds.size)
        Assert.assertEquals("a", breeds[0].name)

        breeds = dao.getBreeds(false)
        Assert.assertEquals(2, breeds.size)
        Assert.assertEquals("b", breeds[0].name)
    }

    @Test
    fun searchBreeds() {
        val breedA = BreedDB(
            name = "Affenpinscher",
            id = 1,
            category = "catA",
            origin = null,
            temperament = "temperamentA"
        )
        val breedB = BreedDB(
            name = "Akbash Dog",
            id = 2,
            category = "catB",
            origin = null,
            temperament = "temperamentB"
        )

        dao.addBreeds(listOf(breedA, breedB))

        val breeds = dao.searchBreeds("b")
        Assert.assertEquals(1, breeds.size)
        Assert.assertEquals("Akbash Dog", breeds[0].name)
    }
}