package com.miguelmialdea.rickandmorty.data.api

import com.miguelmialdea.rickandmorty.data.dto.CharacterResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDTO

}