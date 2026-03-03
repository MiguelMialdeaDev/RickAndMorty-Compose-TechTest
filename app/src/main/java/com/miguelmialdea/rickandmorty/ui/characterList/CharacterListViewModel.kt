package com.miguelmialdea.rickandmorty.ui.characterList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelmialdea.rickandmorty.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
): ViewModel() {

    private val _state = mutableStateOf(CharacterListState())

    val state: State<CharacterListState> = _state

    init {
        getCharacters()
    }

    fun getCharacters() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true, error = null)

            try {
                val result = getCharactersUseCase(1)
                _state.value = _state.value.copy(
                    isLoading = false,
                    characters = result
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "An unexpected error occurred"
                )
            }
        }
    }
}