package com.miguelmialdea.rickandmorty.data.repository

import retrofit2.HttpException
import com.miguelmialdea.rickandmorty.data.api.CharacterApiService
import com.miguelmialdea.rickandmorty.data.local.dao.CharacterDao
import com.miguelmialdea.rickandmorty.data.mapper.toEntity
import com.miguelmialdea.rickandmorty.data.mapper.toModel
import com.miguelmialdea.rickandmorty.domain.exception.DomainException
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import okio.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterApiService,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<CharacterModel>> {
        return try {
            val response = api.getCharacters(page).result.map { it.toModel() }
            characterDao.insertCharacters(response.map { it.toEntity() })
            Result.success(response)
        } catch (e: Exception) {
            val cachedCharacters = characterDao.getAllCharacters().map { it.toModel() }
            if (cachedCharacters.isNotEmpty()) {
                Result.success(cachedCharacters)
            } else {
                val domainException =
                    when (e) {
                        is HttpException -> {
                            when (e.code()) {
                                404 -> DomainException.NotFoundException()
                                429 -> DomainException.RateLimitException()
                                else -> DomainException.UnknownException(
                                    e.message() ?: "HTTP Error ${e.code()}"
                                )
                            }
                        }
                        is IOException -> DomainException.NetworkException()
                        else -> DomainException.UnknownException(
                            e.localizedMessage ?: "Unknown error"
                        )
                    }
                Result.failure(domainException)
            }
        }
    }

    override suspend fun searchCharacters(name: String, page: Int): Result<List<CharacterModel>> {
        return try {
            val response = api.searchCharacters(name, page).result.map { it.toModel() }
            characterDao.insertCharacters(response.map { it.toEntity() })
            Result.success(response)
        } catch (e: Exception) {
            val cachedResults = characterDao.searchCharacters(name).map { it.toModel() }

            if (cachedResults.isNotEmpty()) {
                Result.success(cachedResults)
            } else {
                val domainException = when {
                    e is HttpException -> {
                        when (e.code()) {
                            404 -> DomainException.NotFoundException("No characters found")
                            429 -> DomainException.RateLimitException()
                            else -> DomainException.UnknownException(
                                e.message() ?: "HTTP Error ${e.code()}"
                            )
                        }
                    }

                    e is IOException -> DomainException.NetworkException()
                    else -> DomainException.UnknownException(e.localizedMessage ?: "Unknown error")
                }
                Result.failure(domainException)
            }
        }
    }

    override suspend fun getCharacterById(id: Int): Result<CharacterModel> {
        return try {
            val character = api.getCharacterById(id).toModel()
            characterDao.insertCharacter(character.toEntity())
            Result.success(character)
        } catch (e: Exception) {
            val cachedCharacter = characterDao.getCharacterById(id)

            if (cachedCharacter != null) {
                Result.success(cachedCharacter.toModel())
            } else {
                val domainException = when {
                    e is HttpException -> {
                        when (e.code()) {
                            404 -> DomainException.NotFoundException("Character not found")
                            429 -> DomainException.RateLimitException()
                            else -> DomainException.UnknownException(
                                e.message() ?: "HTTP Error ${e.code()}"
                            )
                        }
                    }

                    e is IOException -> DomainException.NetworkException()
                    else -> DomainException.UnknownException(e.localizedMessage ?: "Unknown error")
                }
                Result.failure(domainException)
            }
        }
    }
}