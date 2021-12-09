package com.example.dogchallenge.api.models

data class Breed(
    val bred_for: String,
    val breed_group: String,
    val height: Measurement,
    val id: Int,
    val image: Image?,
    val life_span: String,
    val name: String,
    val origin: String?,
    val reference_image_id: String,
    val temperament: String,
    val weight: Measurement
)