package com.brewflow.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrewingMethodsResponse(
    @Json(name = "brewingMethods") val brewingMethods: List<BrewingMethodDto>
)

@JsonClass(generateAdapter = true)
data class BrewingMethodDto(
    val id: String,
    val name: String,
    val description: String?,
    val maxCups: Int,
    val coffeeGramsPerCup: Int,
    val waterMlPerCup: Int,
    val grindSize: String?,
    val imageUrl: String?,
    val steps: List<BrewStepDto>,
    val flavorProfiles: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class BrewStepDto(
    val order: Int,
    val title: String,
    val description: String?,
    val timeSeconds: Int = 0,
    val waterMl: Int? = null,
    val imageUrl: String? = null
)
