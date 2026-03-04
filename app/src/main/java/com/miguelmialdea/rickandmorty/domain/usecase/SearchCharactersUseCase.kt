package com.miguelmialdea.rickandmorty.domain.usecase

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(name: String, page: Int): List<CharacterModel> {
        val result = repository.searchCharacters(name, page)
        return result.getOrThrow()
    }
}