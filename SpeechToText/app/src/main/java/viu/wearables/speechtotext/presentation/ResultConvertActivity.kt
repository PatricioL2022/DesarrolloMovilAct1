package viu.wearables.speechtotext.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import viu.wearables.speechtotext.R

import viu.wearables.speechtotext.presentation.components.BasicTopBar
import viu.wearables.speechtotext.presentation.ui.theme.SpeechToTextTheme

class ResultConvertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechToTextTheme {
                val resultadoConvertirAudioTexto = intent.getStringExtra("resultadoConvertirAudioTexto")
                CustomScaffold(resultadoConvertirAudioTexto.toString())
            }
        }
    }
}

@Composable
fun TextoResultado(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        modifier = modifier
    )
}




@Composable
fun CustomScaffold(texto: String) {
    Scaffold(
        // Barra superior
        topBar = { BasicTopBar(stringResource(R.string.resultado_audio_procesado)) },
        // Contenido principal
        content = { padding ->
            CustomContent(padding,texto)
        }
    )
}
@Composable
fun CustomContent(padding: PaddingValues,texto: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextoResultado(texto)
    }
}