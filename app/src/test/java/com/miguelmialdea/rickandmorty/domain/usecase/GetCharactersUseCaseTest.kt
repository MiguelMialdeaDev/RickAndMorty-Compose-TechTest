package com.miguelmialdea.rickandmorty.domain.usecase

import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharactersUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `should return a list of characters`() = runTest {
        val fakeCharacters = listOf(
            CharacterModel(1, "Rick", "Alive", "Human", "Male", "image.jpg", "Earth", "Earth")
        )
        coEvery { repository.getCharacters(1) } returns Result.success(fakeCharacters)

        val result = useCase(1)

        assertEquals(1, result.size)
        assertEquals("Rick", result[0].name)
        coVerify { repository.getCharacters(1) }
    }

    @Test(expected = Exception::class)
    fun `should return error when repository fails`() = runTest {
        coEvery { repository.getCharacters(1) } returns Result.failure(Exception("Error"))

        useCase(1)
    }
}