package com.miguelmialdea.rickandmorty.domain.exception

sealed class DomainException(message: String) : Exception(message) {
    class NotFoundException(message: String = "No more data available") : DomainException(message)
    class RateLimitException(message: String = "Too many requests") : DomainException(message)
    class NetworkException(message: String = "No internet connection") : DomainException(message)
    class UnknownException(message: String = "Unexpected error") : DomainException(message)
}
