package com.miguelmialdea.rickandmorty.ui.characterDetail

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

data class CharacterDetailState(
    val isLoading: Boolean = false,
    val character: CharacterModel? = null,
    val error: String? = null
)