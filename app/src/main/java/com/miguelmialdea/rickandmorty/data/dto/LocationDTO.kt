package com.miguelmialdea.rickandmorty.data.dto

import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("name") val name: String,
)