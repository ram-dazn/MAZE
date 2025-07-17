package com.maze.mobile.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.maze.mobile.data.api.MazeApi
import com.maze.mobile.data.implementation.AndroidMazeParser
import com.maze.mobile.data.implementation.MazeRepository
import com.maze.mobile.domain.api.MazeParserApi
import com.maze.mobile.domain.api.MazeRepositoryApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindMazeRepository(
        impl: MazeRepository,
    ): MazeRepositoryApi

    @Binds
    abstract fun bindMazeParser(
        impl: AndroidMazeParser,
    ): MazeParserApi
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = "https://downloads-secured.bluebeam.com/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        client: OkHttpClient,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit): MazeApi =
        retrofit.create(MazeApi::class.java)
}