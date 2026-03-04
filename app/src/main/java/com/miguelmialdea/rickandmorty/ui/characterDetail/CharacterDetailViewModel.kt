package com.miguelmialdea.rickandmorty.ui.characterDetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.miguelmialdea.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.miguelmialdea.rickandmorty.domain.exception.DomainException
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(CharacterDetailState())
    val state: State<CharacterDetailState> = _state

    init {
        val characterId = savedStateHandle.get<Int>("characterId")
        characterId?.let { loadCharacter(it) }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                val character = getCharacterByIdUseCase(id)

                _state.value = _state.value.copy(
                    character = character,
                    isLoading = false
                )
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is DomainException.NotFoundException -> "Character not found"
                    is DomainException.NetworkException -> "No internet connection"
                    else -> e.localizedMessage ?: "Error loading character"
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }
}