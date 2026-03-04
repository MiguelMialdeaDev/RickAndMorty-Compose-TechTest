package com.miguelmialdea.rickandmorty.data.api

import com.miguelmialdea.rickandmorty.data.dto.CharacterDTO
import com.miguelmialdea.rickandmorty.data.dto.CharacterResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDTO

    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name: String,
        @Query("page") page: Int = 1
    ): CharacterResponseDTO

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDTO
}