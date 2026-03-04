package com.miguelmialdea.rickandmorty.ui.characterList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelmialdea.rickandmorty.domain.exception.DomainException
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

    private var currentPage = 1
    private var isLastPage = false
    private var isFetching = false

    init {
        loadNextCharacters()
    }

    fun loadNextCharacters() {
        if (isFetching || isLastPage) return

        viewModelScope.launch {
            try {
                isFetching = true
                _state.value = state.value.copy(isLoading = true, error = null)

                val result = getCharactersUseCase(currentPage)

                if (result.isEmpty()) {
                    isLastPage = true
                    _state.value = _state.value.copy(isLoading = false)
                } else {
                    _state.value = _state.value.copy(
                        characters = _state.value.characters + result,
                        isLoading = false,
                        error = null
                    )
                    currentPage++
                }
            } catch (e: Exception) {
                when (e) {
                    is com.miguelmialdea.rickandmorty.domain.exception.DomainException.NotFoundException -> {
                        isLastPage = true
                        _state.value = _state.value.copy(isLoading = false)
                    }

                    is com.miguelmialdea.rickandmorty.domain.exception.DomainException.RateLimitException -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Too many requests. Please wait..."
                        )
                    }

                    else -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Unexpected error"
                        )
                    }
                }
            } finally {
                isFetching = false
            }
        }
    }
}