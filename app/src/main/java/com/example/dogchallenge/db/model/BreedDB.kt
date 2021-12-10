package com.example.dogchallenge.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dogchallenge.api.models.Breed

@Entity(tableName = "breeds")
class BreedDB(
    @PrimaryKey val id: Int,
    val name: String,
    val temperament: String,
    val origin: String? = "",
    val category: String = ""
) {
    constructor(breed: Breed) : this(breed.id, breed.name, breed.temperament, breed.origin)
}