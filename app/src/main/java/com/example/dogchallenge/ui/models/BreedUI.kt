package com.example.dogchallenge.ui.models

import android.os.Parcelable
import com.example.dogchallenge.api.models.Breed
import kotlinx.parcelize.Parcelize


@Parcelize
data class BreedUI(
    val name: String,
    val url: String?,
    val category: String,
    val origin: String?,
    val temperament: String?
) : Parcelable {
    constructor(breed: Breed) : this(
        breed.name,
        breed.image?.url,
        "",
        breed.origin,
        breed.temperament
    )
}
