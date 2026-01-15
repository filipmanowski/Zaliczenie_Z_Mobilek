package pl.kul.aplikacjemobilneprojektzaliczeniowy.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
}
