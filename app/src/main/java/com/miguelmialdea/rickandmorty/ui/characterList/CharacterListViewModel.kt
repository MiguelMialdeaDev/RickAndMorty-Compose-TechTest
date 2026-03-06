package com.miguelmialdea.rickandmorty.ui.characterList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelmialdea.rickandmorty.domain.exception.DomainException
import com.miguelmialdea.rickandmorty.domain.usecase.GetCharactersUseCase
import com.miguelmialdea.rickandmorty.domain.usecase.SearchCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase
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

                val result = if (_state.value.searchQuery.isNotBlank()) {
                    searchCharactersUseCase(_state.value.searchQuery.trim(), currentPage)
                } else {
                    getCharactersUseCase(currentPage)
                }

                if (result.isEmpty()) {
                    isLastPage = true
                    _state.value = _state.value.copy(isLoading = false)
                } else {
                    val currentIds = _state.value.characters.map { it.id }.toSet()
                    val newCharacters = result.filter { it.id !in currentIds }

                    _state.value = _state.value.copy(
                        characters = _state.value.characters + newCharacters,
                        isLoading = false,
                        error = null
                    )
                    currentPage++
                }
            } catch (e: Exception) {
                when (e) {
                    is DomainException.NotFoundException -> {
                        isLastPage = true
                        _state.value = _state.value.copy(isLoading = false)
                    }

                    is DomainException.RateLimitException -> {
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

    fun searchCharacters(query: String) {
        _state.value = _state.value.copy(searchQuery = query)

        currentPage = 1
        isLastPage = false
        isFetching = false

        _state.value = _state.value.copy(
            characters = emptyList(),
            error = null
        )

        loadNextCharacters()
    }
}