package com.miguelmialdea.rickandmorty.data.repository

import retrofit2.HttpException
import com.miguelmialdea.rickandmorty.data.api.CharacterApiService
import com.miguelmialdea.rickandmorty.data.mapper.toModel
import com.miguelmialdea.rickandmorty.domain.exception.DomainException
import com.miguelmialdea.rickandmorty.domain.model.CharacterModel
import com.miguelmialdea.rickandmorty.domain.repository.CharacterRepository
import okio.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterApiService
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<CharacterModel>> {
        return try {
            val response = api.getCharacters(page)
            Result.success(response.result.map { it.toModel() })
        } catch (e: Exception) {
            val domainException =
                when (e) {
                 is HttpException -> {
                    when (e.code()) {
                        404 -> DomainException.NotFoundException()
                        429 -> DomainException.RateLimitException()
                        else -> DomainException.UnknownException(e.message() ?: "HTTP Error ${e.code()}")
                    }
                }
                 is IOException -> DomainException.NetworkException()
                else -> DomainException.UnknownException(e.localizedMessage ?: "Unknown error")
            }
            Result.failure(domainException)
        }
    }
}