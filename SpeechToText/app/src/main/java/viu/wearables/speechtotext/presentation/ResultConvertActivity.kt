package viu.wearables.speechtotext.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import viu.wearables.speechtotext.R
import viu.wearables.speechtotext.presentation.ui.theme.SpeechToTextTheme

class ResultConvertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechToTextTheme {
                val resultadoConvertirAudioTexto = intent.getStringExtra("resultadoConvertirAudioTexto")
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TextoResultado(
                        name = stringResource(R.string.resultado_audio_procesado) + resultadoConvertirAudioTexto.toString(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TextoResultado(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SpeechToTextTheme {
        TextoResultado("Android")
    }
}