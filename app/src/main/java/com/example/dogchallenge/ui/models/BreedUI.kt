package com.example.dogchallenge.ui.models

import android.os.Parcelable
import com.example.dogchallenge.api.models.Breed
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BreedUI(
    val name: String,
    val category: String,
    val origin: String?,
    val temperament: String
) : Parcelable {
    constructor(breed: Breed) : this(breed.name, "", breed.origin, breed.temperament)
}
