package viu.wearables.speechtotext.presentation.interfaces.list

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import viu.wearables.speechtotext.utils.Screen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import viu.wearables.speechtotext.R
import viu.wearables.speechtotext.presentation.HistoryEvent
import viu.wearables.speechtotext.presentation.components.BasicTopBar
import viu.wearables.speechtotext.presentation.components.HistoryCard

@Composable
fun ListHistoryScreen (navController: NavController, historiesViewModel: ListHistoryViewModel) {
    val snackbarHostState = remember {SnackbarHostState()}
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost ={ SnackbarHost(snackbarHostState)},
        topBar = { BasicTopBar(stringResource(R.string.historial)) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditHistoryScreen.route)
            },
                modifier = Modifier.background(Color.White)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.agregar_historial))
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            LazyColumn {
                historiesViewModel.histories.value.forEach{ history ->
                    item{
                        HistoryCard(history, onDeleteClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Historial eliminado")
                            }

                            historiesViewModel.onEvent(HistoryEvent.Delete(history))
                        },
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.AddEditHistoryScreen.route + "?historyId=${history.id}")
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}