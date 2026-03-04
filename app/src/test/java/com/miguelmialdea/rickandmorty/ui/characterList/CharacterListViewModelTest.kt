package com.miguelmialdea.rickandmorty.ui.characterList

import com.miguelmialdea.rickandmorty.domain.exception.DomainException
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.usecase.GetCharactersUseCase
import com.miguelmialdea.rickandmorty.domain.usecase.SearchCharactersUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var searchCharactersUseCase: SearchCharactersUseCase
    private lateinit var viewModel: CharacterListViewModel

    private val fakeCharacters = listOf(
        CharacterModel(1, "Rick Sanchez", "Alive", "Human", "Male", "img.jpg", "Earth", "Earth"),
        CharacterModel(2, "Morty Smith", "Alive", "Human", "Male", "img.jpg", "Earth", "Earth")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCharactersUseCase = mockk()
        searchCharactersUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads first page of characters auto`() = runTest {
        coEvery { getCharactersUseCase(1) } returns fakeCharacters

        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()

        coVerify { getCharactersUseCase(1) }
        assertEquals(2, viewModel.state.value.characters.size)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loadNextCharacters loads next page`() = runTest {
        coEvery { getCharactersUseCase(1) } returns fakeCharacters
        coEvery { getCharactersUseCase(2) } returns listOf(
            CharacterModel(3, "Summer Smith", "Alive", "Human", "Female", "img.jpg", "Earth", "Earth")
        )

        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()
        viewModel.loadNextCharacters()
        advanceUntilIdle()

        assertEquals(3, viewModel.state.value.characters.size)
    }

    @Test
    fun `searchCharacters resets list with search results`() = runTest {
        coEvery { getCharactersUseCase(1) } returns fakeCharacters
        coEvery { searchCharactersUseCase("Rick", 1) } returns listOf(fakeCharacters[0])

        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()
        viewModel.searchCharacters("Rick")
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.characters.size)
        assertEquals("Rick Sanchez", viewModel.state.value.characters[0].name)
        assertEquals("Rick", viewModel.state.value.searchQuery)
    }

    @Test
    fun `empty query loads normal size`() = runTest {
        coEvery { getCharactersUseCase(1) } returns fakeCharacters
        coEvery { searchCharactersUseCase("Rick", 1) } returns listOf(fakeCharacters[0])

        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()
        viewModel.searchCharacters("Rick")
        advanceUntilIdle()
        viewModel.searchCharacters("")
        advanceUntilIdle()

        assertEquals("", viewModel.state.value.searchQuery)
        assertEquals(2, viewModel.state.value.characters.size)
    }

    @Test
    fun `error sets error state`() = runTest {
        // Given
        coEvery { getCharactersUseCase(1) } returns fakeCharacters
        coEvery { getCharactersUseCase(2) } throws DomainException.NetworkException()

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()
        viewModel.loadNextCharacters()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.error != null)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `empty result stops pagination`() = runTest {
        // Given
        coEvery { getCharactersUseCase(1) } returns fakeCharacters
        coEvery { getCharactersUseCase(2) } returns emptyList()

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase, searchCharactersUseCase)
        advanceUntilIdle()
        viewModel.loadNextCharacters()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(2, viewModel.state.value.characters.size) // No añade más
    }
}