package com.miguelmialdea.rickandmorty.data.mapper

import com.miguelmialdea.rickandmorty.data.dto.CharacterDTO
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

fun CharacterDTO.toModel() =
    CharacterModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image,
        origin = this.origin.name
    )