package com.miguelmialdea.rickandmorty.domain.repository

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Result<List<CharacterModel>>
}