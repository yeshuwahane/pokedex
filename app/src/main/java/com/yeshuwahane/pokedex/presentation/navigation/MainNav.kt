package com.yeshuwahane.pokedex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.PokemonDetailScreen
import com.yeshuwahane.pokedex.presentation.feature.home.HomeScreen


@Composable
fun MainNav() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail/{id}") {
            val id = it.arguments?.getString("id")?.toInt() ?: 0
            PokemonDetailScreen(id, onBackClick = {
                navController.popBackStack()
            })
        }
    }

}