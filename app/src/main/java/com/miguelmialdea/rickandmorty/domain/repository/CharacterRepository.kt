package com.miguelmialdea.rickandmorty.domain.repository

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Result<List<CharacterModel>>
    suspend fun searchCharacters(name: String, page: Int): Result<List<CharacterModel>>
    suspend fun getCharacterById(id: Int): Result<CharacterModel>
}