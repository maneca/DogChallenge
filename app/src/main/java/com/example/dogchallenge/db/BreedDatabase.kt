package com.example.dogchallenge.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogchallenge.db.dao.BreedDao
import com.example.dogchallenge.db.model.BreedDB

@Database(
    entities = [BreedDB::class],
    version = 1, exportSchema = false
)

abstract class BreedDatabase : RoomDatabase() {
    abstract val breedDao: BreedDao
}