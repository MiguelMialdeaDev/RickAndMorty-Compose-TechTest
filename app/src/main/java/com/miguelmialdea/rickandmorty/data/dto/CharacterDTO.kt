package com.miguelmialdea.rickandmorty.data.dto

import com.google.gson.annotations.SerializedName

data class CharacterDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("image") val image: String,
    @SerializedName("origin") val origin: OriginDTO,
    @SerializedName("location") val location: LocationDTO
)