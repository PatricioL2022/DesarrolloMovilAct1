package viu.wearables.speechtotext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.room.Room
import viu.wearables.speechtotext.data.HistoryDatabase
import viu.wearables.speechtotext.presentation.interfaces.list.ListHistoryScreen
import viu.wearables.speechtotext.presentation.interfaces.list.ListHistoryViewModel
import viu.wearables.speechtotext.ui.theme.SpeechToTextTheme
import viu.wearables.speechtotext.utils.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import viu.wearables.speechtotext.presentation.interfaces.addedit.AddEditHistoryScreen
import viu.wearables.speechtotext.presentation.interfaces.addedit.AddEditHistoryViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost


class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java,
            HistoryDatabase.DATABASE_NAME
        ).build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpeechToTextTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.HistoryListScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.HistoryListScreen.route) {
                            val histories = viewModel<ListHistoryViewModel>{
                                ListHistoryViewModel(db.dao)
                            }
                            ListHistoryScreen(navController, histories)
                        }

                        composable(route = Screen.AddEditHistoryScreen.route+"?historyId={historyId}",
                            arguments = listOf(
                                navArgument(name = "historyId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )) { navBackStackEntry ->

                            val musicId = navBackStackEntry.arguments?.getInt("historyId") ?: -1

                            val song = viewModel<AddEditHistoryViewModel>() {
                                AddEditHistoryViewModel(db.dao,musicId)
                            }
                            AddEditHistoryScreen(navController, song)
                        }
                    }

                }
            }
        }
    }
}