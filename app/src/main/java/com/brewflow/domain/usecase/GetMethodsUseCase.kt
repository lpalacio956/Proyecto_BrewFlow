package com.brewflow.domain.usecase

import com.brewflow.domain.repository.BrewRepository
import javax.inject.Inject

class GetMethodsUseCase @Inject constructor(
    private val repo: BrewRepository
) {
    suspend operator fun invoke() = repo.getMethods()
}
