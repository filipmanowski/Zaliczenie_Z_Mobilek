package pl.kul.aplikacjemobilneprojektzaliczeniowy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens.AddTransactionScreen
import pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onAddClick = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onEditClick = { id ->
                    navController.navigate(Screen.EditTransaction.createRoute(id))
                }
            )
        }

        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                transactionId = null,
                onBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EditTransaction.route) { backStackEntry ->
            val id = backStackEntry.arguments!!
                .getString("id")!!
                .toInt()

            AddTransactionScreen(
                transactionId = id,
                onBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
