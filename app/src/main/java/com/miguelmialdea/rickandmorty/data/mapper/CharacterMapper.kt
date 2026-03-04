package com.miguelmialdea.rickandmorty.data.mapper

import com.miguelmialdea.rickandmorty.data.dto.CharacterDTO
import com.miguelmialdea.rickandmorty.data.local.entity.CharacterEntity
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

fun CharacterDTO.toModel() =
    CharacterModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image,
        origin = this.origin.name,
        location = this.location.name
    )

fun CharacterEntity.toModel() =
    CharacterModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image,
        origin = this.origin,
        location = this.location
    )

fun CharacterModel.toEntity() =
    CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image,
        origin = this.origin,
        location = this.location,
        timestamp = System.currentTimeMillis()
    )