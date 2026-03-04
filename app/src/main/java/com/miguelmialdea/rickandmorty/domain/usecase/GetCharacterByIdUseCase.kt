package com.miguelmialdea.rickandmorty.domain.usecase

import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.getCharacterById(id).getOrThrow()
}