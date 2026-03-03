package com.miguelmialdea.rickandmorty.data.dto

import com.google.gson.annotations.SerializedName

data class CharacterResponseDTO(
    @SerializedName("results") val result: List<CharacterDTO>
)