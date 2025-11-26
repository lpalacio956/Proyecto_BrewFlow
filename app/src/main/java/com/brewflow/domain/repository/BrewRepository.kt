package com.brewflow.domain.repository

import com.brewflow.data.model.BrewingMethodDto

interface BrewRepository {
    suspend fun getMethods(): List<BrewingMethodDto>
}
