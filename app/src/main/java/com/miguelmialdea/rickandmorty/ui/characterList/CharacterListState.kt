package com.miguelmialdea.rickandmorty.ui.characterList

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel

data class CharacterListState(
    val isLoading: Boolean = false,
    val characters: List<CharacterModel> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)