package viu.wearables.speechtotext.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import viu.wearables.speechtotext.R
import viu.wearables.speechtotext.data.HistoryDatabase
import viu.wearables.speechtotext.presentation.components.BasicButton
import viu.wearables.speechtotext.presentation.components.BasicTopBar
import viu.wearables.speechtotext.presentation.models.History
import viu.wearables.speechtotext.presentation.ui.theme.SpeechToTextTheme
import viu.wearables.speechtotext.retrofit.InitializeRetrofit
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Pantalla para seleccionar un archivo de audio desde el almacenamiento externo
 * del dispositivo, archivos permitidos .mp3
 */

class ImportFileActivity : ComponentActivity() {

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
        rutaArchivo=""

        setContent {
            SpeechToTextTheme {
                CustomScaffold()

            }
        }
    }
    companion object {
        lateinit var nombreArchivo: String
        lateinit var rutaArchivo: String
        lateinit var mMediaPlayer: MediaPlayer
    }
    private val requestFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    handleSelectedFile(uri)
                }
            }
        }
    private fun openSpecificFolder(folderName: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"  // Set the MIME type to filter files
            val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:$folderName")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        requestFileLauncher.launch(intent)
    }
    @SuppressLint("Range")
    private fun handleSelectedFile(uri: android.net.Uri) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                ImportFileActivity.nombreArchivo = displayName
                rutaArchivo = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/${ImportFileActivity.nombreArchivo}"
                mMediaPlayer = MediaPlayer.create(this.applicationContext, Uri.parse(rutaArchivo))
            }
        }
    }

    fun llamaApi(){
        val body = File(rutaArchivo)
        val requestFile: RequestBody =
            RequestBody.create(
                "application/octet-stream".toMediaType(),
                body
            )
        val multipartBody = MultipartBody.Part.createFormData("elmo","elmo.mp3",requestFile)
        GlobalScope.launch {
            val result = InitializeRetrofit.todoApi.subirAudio(multipartBody);
            if (result != null){
                val fechaFormato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val horaFormato = DateTimeFormatter.ofPattern("HH:mm")
                val fechaActual = LocalDateTime.now().format(fechaFormato)
                val horaActual = LocalDateTime.now().format(horaFormato)
                var history: History =History(transcripcion = result.transcript,
                    nombreAudio = ImportFileActivity.nombreArchivo,
                    fecha = fechaActual, hora = horaActual,
                    confianza = result.confidence, tiempoProcesamiento = result.seconds,
                    unidadMedidaTiempoProcesamiento = "segundos")
                db.dao.upsertHistory(history)
                var resultadoTextoProcesado = result
                val resulConvertActivity = Intent(this@ImportFileActivity, ResultConvertActivity::class.java)
                resulConvertActivity.putExtra("resultadoConvertirAudioTexto", resultadoTextoProcesado.transcript)
                startActivity(resulConvertActivity)
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun CustomScaffold() {
        Scaffold(
            // Barra superior
            topBar = { BasicTopBar(stringResource(R.string.importa_tu_archivo)) },
            // Contenido principal
            content = { padding ->
                CustomContent(padding)
            }
        )
    }
    @Composable
    fun PlayerContent() {
        val mContext = LocalContext.current
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.elmo)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                // Boton play
                IconButton(onClick = { mMediaPlayer.start() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "",
                        Modifier.size(100.dp)
                    )
                }

                // Boton Pausa
                IconButton(onClick = { mMediaPlayer.pause() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "",
                        Modifier.size(100.dp)
                    )
                }
            }
        }

    }
    @Composable
    fun CustomContent(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlayerContent()
            BasicButton(
                onClick = {
                    openSpecificFolder("music")
                },
                texto = stringResource(R.string.importar_audio)
            )
            BasicButton(
                onClick = {
                    llamaApi()

                },
                texto = stringResource(R.string.convertir_audio)
            )
            BasicButton(
                onClick = {
                    val historyActivity = Intent(this@ImportFileActivity, HistoryActivity::class.java)
                    startActivity(historyActivity)
                },
                texto = stringResource(R.string.historial)
            )

        }
    }
}




