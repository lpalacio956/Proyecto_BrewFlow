package com.brewflow.data.repository

import com.brewflow.data.remote.BrewApi
import com.brewflow.domain.repository.BrewRepository
import javax.inject.Inject

class BrewRepositoryImpl @Inject constructor(
    private val api: BrewApi
) : BrewRepository {
    override suspend fun getMethods() = api.getBrewingMethods().brewingMethods
}
