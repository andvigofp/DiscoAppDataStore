package com.example.discoappdatastore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.discoappdatastore.ui.screens.AddDiscoScreen
import com.example.discoappdatastore.ui.screens.DetalleScreen
import com.example.discoappdatastore.ui.screens.HomeScreen
import com.example.discoappdatastore.ui.state.DiscoViewModel

@Composable
fun ListaDiscosApp(viewModel: DiscoViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DiscosScreen.HomeScreen.route
    ) {
        composable(DiscosScreen.HomeScreen.route) {
            HomeScreen(
                viewModel = viewModel,
                onDiscoClick = { discoId ->
                    navController.navigate(DiscosScreen.DetalleScreen.createRoute(discoId))
                },
                onAddClick = {
                    navController.navigate(DiscosScreen.AddDiscoScreen.route)
                }
            )
        }
        composable(
            route = DiscosScreen.DetalleScreen.route,
            arguments = listOf(navArgument("discoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val discoId = backStackEntry.arguments?.getInt("discoId") ?: 0
            DetalleScreen(
                viewModel = viewModel,
                discoId = discoId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(DiscosScreen.AddDiscoScreen.route) {
            AddDiscoScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}