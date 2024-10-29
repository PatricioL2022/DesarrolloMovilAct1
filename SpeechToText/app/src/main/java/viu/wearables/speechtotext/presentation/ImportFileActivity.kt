package viu.wearables.speechtotext.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import viu.wearables.speechtotext.MainActivity
import viu.wearables.speechtotext.R
import viu.wearables.speechtotext.data.HistoryDatabase
import viu.wearables.speechtotext.presentation.components.BasicButton
import viu.wearables.speechtotext.presentation.components.BasicImage
import viu.wearables.speechtotext.presentation.components.BasicTopBar
import viu.wearables.speechtotext.presentation.list.SpeechToTextViewModel
import viu.wearables.speechtotext.presentation.models.History
import viu.wearables.speechtotext.presentation.ui.theme.SpeechToTextTheme
import viu.wearables.speechtotext.repository.Repository
import viu.wearables.speechtotext.retrofit.InitializeRetrofit
import java.io.File

/**
 * Pantalla para seleccionar un archivo de audio desde el almacenamiento externo
 * del dispositivo, archivos permitidos .mp3
 */

class ImportFileActivity : ComponentActivity() {
    //private val speechToTextViewModel by viewModels<SpeechToTextViewModel>(Repository(),)
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
                CustomScaffold()

            }
        }
    }
    companion object {
        lateinit var nombreArchivo: String
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
                Log.d("viu.wearables.speechtotext",uri.path.toString())
                //val resulConvertActivity = Intent(this@ImportFileActivity, ResultConvertActivity::class.java)
                //resulConvertActivity.putExtra("resultadoConvertirAudioTexto", "hola mundo")
                //startActivity(resulConvertActivity)

                ImportFileActivity.nombreArchivo = displayName
                llamaApi()
                //val intent : Intent(this,ResulConvertActivity.class)

            }
        }
    }

    fun llamaApi(){
        val body = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/${ImportFileActivity.nombreArchivo}")
        val requestFile: RequestBody =
            RequestBody.create(
                "application/octet-stream".toMediaType(),
                body
            )
        val multipartBody = MultipartBody.Part.createFormData("elmo","elmo.mp3",requestFile)
        // val repository : Repository()
        // repository.subirAudio(multipartBody)
       // speechToTextViewModel.speechToText
        GlobalScope.launch {
            val result = InitializeRetrofit.todoApi.subirAudio(multipartBody);
            if (result != null){

                var history:History=History(transcripcion = result.transcript,
                    nombreAudio = ImportFileActivity.nombreArchivo,
                    fecha = "29/10/2024", hora = "5:23",
                    confianza = result.confidence, tiempoProcesamiento = result.seconds,
                    unidadMedidaTiempoProcesamiento = "segundos")
                db.dao.upsertHistory(history)
                var resultadoTextoProcesado = result
                val resulConvertActivity = Intent(this@ImportFileActivity, ResultConvertActivity::class.java)
                resulConvertActivity.putExtra("resultadoConvertirAudioTexto", resultadoTextoProcesado.transcript)
                startActivity(resulConvertActivity)

                Log.d("AYUDA: ", result.toString())
            }
            // Checking the results

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
    fun CustomContent(padding: PaddingValues) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BasicImage()
            BasicButton(
                onClick = {
                    openSpecificFolder("music")
                    Toast.makeText(context, "Pero que ha pasao", Toast.LENGTH_LONG).show()
                },
                texto = stringResource(R.string.importar_audio)
            )
            BasicButton(
                onClick = {
                    val historyActivity = Intent(this@ImportFileActivity, MainActivity::class.java)
                    startActivity(historyActivity)
                },
                texto = stringResource(R.string.historial)
            )
        }
    }
}




