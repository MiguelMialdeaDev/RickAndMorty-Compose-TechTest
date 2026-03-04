package com.miguelmialdea.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miguelmialdea.rickandmorty.ui.characterDetail.CharacterDetailScreen
import com.miguelmialdea.rickandmorty.ui.characterDetail.CharacterDetailViewModel
import com.miguelmialdea.rickandmorty.ui.characterList.CharacterListScreen
import com.miguelmialdea.rickandmorty.ui.characterList.CharacterListViewModel
import com.miguelmialdea.rickandmorty.ui.nav.Routes
import com.miguelmialdea.rickandmorty.ui.theme.RickAndMortyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.CharacterListScreen.route
                ) {
                    composable(Routes.CharacterListScreen.route) {
                        val viewModel: CharacterListViewModel = hiltViewModel()
                        CharacterListScreen(
                            viewModel = viewModel,
                            onCharacterClick = { characterId ->
                                navController.navigate(Routes.CharacterDetailScreen.createRoute(characterId))
                            }
                        )
                    }

                    composable(
                        route = Routes.CharacterDetailScreen.route,
                        arguments = listOf(
                            navArgument("characterId") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        val viewModel: CharacterDetailViewModel = hiltViewModel()
                        CharacterDetailScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}