package com.miguelmialdea.rickandmorty.data.repository

import com.miguelmialdea.rickandmorty.data.api.CharacterApiService
import com.miguelmialdea.rickandmorty.data.mapper.toModel
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterApiService
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): List<CharacterModel> {
        return api.getCharacters(page).result.map { it.toModel() }
    }
}