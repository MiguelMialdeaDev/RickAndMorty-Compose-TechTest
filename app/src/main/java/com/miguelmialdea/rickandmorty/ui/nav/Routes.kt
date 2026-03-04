package com.miguelmialdea.rickandmorty.ui.nav

sealed class Routes(val route: String) {
    object CharacterListScreen : Routes("character_list")
    object CharacterDetailScreen : Routes("character_detail/{characterId}") {
        fun createRoute(characterId: Int) = "character_detail/$characterId"
    }
}