package com.example.dogchallenge.api.models

import com.example.dogchallenge.db.model.BreedDB

data class Breed(
    val bred_for: String,
    val breed_group: String?,
    val height: Measurement?,
    val id: Int,
    val image: Image?,
    val life_span: String,
    val name: String,
    val origin: String?,
    val reference_image_id: String,
    val temperament: String?,
    val weight: Measurement?
) {
    constructor(breedDB: BreedDB) : this(
        "",
        "",
        null,
        breedDB.id,
        null,
        "",
        breedDB.name,
        breedDB.origin,
        "",
        breedDB.temperament,
        null
    )
}