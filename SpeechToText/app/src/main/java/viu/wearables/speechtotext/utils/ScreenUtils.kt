package viu.wearables.speechtotext.utils

sealed class Screen (val route: String){
    data object HistoryListScreen : Screen("history_list_screen")
    data object AddEditHistoryScreen : Screen("add_edit_history_screen")
}