package com.example.dogchallenge.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dogchallenge.db.model.BreedDB

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds ORDER BY CASE WHEN :order = 1 THEN name END ASC, CASE WHEN :order = 0 THEN name END DESC")
    fun getBreeds(order: Boolean): List<BreedDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBreeds(breeds: List<BreedDB>)

    @Query("SELECT * FROM breeds WHERE name LIKE '%' || :query || '%'")
    fun searchBreeds(query: String): List<BreedDB>
}