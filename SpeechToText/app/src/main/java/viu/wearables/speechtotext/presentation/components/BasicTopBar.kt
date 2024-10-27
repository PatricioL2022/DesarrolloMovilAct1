package viu.wearables.speechtotext.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(titulo: String) {
    val materialBlue700= Color(red=2, green =117, blue= 216)
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