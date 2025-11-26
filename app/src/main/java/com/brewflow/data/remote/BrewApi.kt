package com.brewflow.data.remote

import com.brewflow.data.model.BrewingMethodsResponse
import retrofit2.http.GET

interface BrewApi {
    @GET("backend/apitemplate/get/896668678538395/brewingMethods")
    suspend fun getBrewingMethods(): BrewingMethodsResponse
}
