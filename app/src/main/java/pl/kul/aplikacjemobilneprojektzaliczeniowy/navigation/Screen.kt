package pl.kul.aplikacjemobilneprojektzaliczeniowy.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object EditTransaction : Screen("edit_transaction/{id}") {
        fun createRoute(id: Int) = "edit_transaction/$id"
    }
}
