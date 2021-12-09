package com.example.dogchallenge.repository.models

import com.example.dogchallenge.api.models.Breed

data class BreedsInfo(
    val totalItems: Int,
    val breeds: List<Breed>
)
