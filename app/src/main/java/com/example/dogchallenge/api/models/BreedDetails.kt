package com.example.dogchallenge.api.models

data class BreedDetails(
    val id: Int,
    val name: String,
    val bred_for: String,
    val breed_group: String,
    val life_span: String,
    val temperament: String,
    val reference_image_id: String,
    val weight: Measurement,
    val metric: String
)


data class Measurement(
    val imperial: String,
    val metric: String
)