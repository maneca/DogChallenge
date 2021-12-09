package com.example.dogchallenge.api.models

data class Breed(
    val breeds: List<BreedDetails>,
    val url: String,
    val width: Int,
    val height: Int
)