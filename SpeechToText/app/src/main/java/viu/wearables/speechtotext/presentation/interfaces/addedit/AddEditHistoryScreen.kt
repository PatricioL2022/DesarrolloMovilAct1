package viu.wearables.speechtotext.presentation.interfaces.addedit

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import viu.wearables.speechtotext.utils.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import viu.wearables.speechtotext.R
import viu.wearables.speechtotext.presentation.AddEditHistoryEvent
import viu.wearables.speechtotext.presentation.components.BasicTopBar

/**
 * Pantalla para mostrar un formulario con todos los campos de un item
 * historial de audios procesados
 */
@Composable
fun AddEditHistoryScreen (
    navController: NavHostController,
    viewModel: AddEditHistoryViewModel
)
{
    Scaffold(
        topBar = { BasicTopBar(stringResource(R.string.editar)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditHistoryEvent.SaveHistory)
                    navController.navigate(Screen.HistoryListScreen.route)
                },
            )
            {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Save history")
            }
        }
    ) { contentPadding ->
        val history = viewModel.history.value
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            OutlinedTextField(
                value = history.nombreAudio,
                label = { Text(stringResource(id = R.string.nombre_audio)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredNombreAudio(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),

            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.transcripcion,
                label = { Text(stringResource(id = R.string.transcripcion)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredTranscripcion(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.fecha,
                label = { Text(stringResource(id = R.string.fecha)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredFecha(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.hora,
                label = { Text(stringResource(id = R.string.hora)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredHora(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.confianza.toString(),
                label = { Text(stringResource(id = R.string.confianza)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredConfianza(it.toFloat()))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.tiempoProcesamiento.toString(),
                label = { Text(stringResource(id = R.string.tiempo_procesamiento)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredTiempoProcesamiento(it.toFloat()))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = history.unidadMedidaTiempoProcesamiento,
                label = { Text(stringResource(id = R.string.unidad_procesamiento)) },
                onValueChange = {
                    viewModel.onEvent(AddEditHistoryEvent.EnteredUnidadProcesamiento(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}