package com.brewflow.di

import com.brewflow.data.remote.BrewApi
import com.brewflow.data.repository.BrewRepositoryImpl
import com.brewflow.domain.repository.BrewRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory // Correct import for Moshi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface BindModule {
    @Binds
    fun bindBrewRepository(impl: BrewRepositoryImpl): BrewRepository
}


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 1. You provide a Moshi instance
    @JvmStatic
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @JvmStatic
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    // 2. You inject Moshi into your Retrofit provider
    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mockfast.io/")
            .client(ok)
            // 3. You use MoshiConverterFactory with the provided Moshi instance
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideBrewApi(retrofit: Retrofit): BrewApi = retrofit.create(BrewApi::class.java)
}
