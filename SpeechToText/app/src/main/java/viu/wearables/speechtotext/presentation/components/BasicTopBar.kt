package viu.wearables.speechtotext.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

/**
 * Componente tipo TopAppBar que muestra información relacionadas con la pantalla actual
 * como el título
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(titulo: String) {
    TopAppBar(
        // Título de la barra superior
        title = { Text(text = titulo) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
    )
}