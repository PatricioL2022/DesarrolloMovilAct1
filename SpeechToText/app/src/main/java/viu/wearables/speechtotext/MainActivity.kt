package viu.wearables.speechtotext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.squareup.okhttp.Headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import viu.wearables.speechtotext.repository.Repository
import viu.wearables.speechtotext.retrofit.InitializeRetrofit
import viu.wearables.speechtotext.ui.theme.SpeechToTextTheme
import java.io.File


class MainActivity : ComponentActivity() {

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
                MainActivity.nombreArchivo = displayName
                switchDispatcher()
                /**val storageDir =
                    File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/VIU")
                var success = true
                var savedImagePath: String? = null
                val imageFileName = "ramon.jpg"
                if (!storageDir.exists()) {
                    success = storageDir.mkdirs()
                }
                if (success) {
                    val imageFile = File(storageDir, imageFileName)
                    savedImagePath = imageFile.absolutePath

                    try {
                        val fOut = FileOutputStream(imageFile)
                        image.compress(Bitmap.CompressFormat.JPEG, 80, fOut)
                        fOut.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    galleryAddPic(savedImagePath)
                }*/

                // Do something with the file name
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MainActivity.appContext = applicationContext
        setContent {
            SpeechToTextTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    nombreArchivo = "elmo.mp3"
                    llamaApi()
                    //openSpecificFolder("music")
                   // switchDispatcher()
                }
            }
        }
    }
    companion object {
        lateinit  var appContext: Context
        lateinit var nombreArchivo: String
    }


}

private fun switchDispatcher() {
    val scope = CoroutineScope(Dispatchers.Main)

    scope.launch {
        val user = withContext(Dispatchers.IO) {
            // some long task for retrieve user from REST or database
            SendRequest()

            //postAndImage(uri,"http://192.168.100.156:80/")
        }

    }
}
/**@RequiresApi(Build.VERSION_CODES.S)
inline fun packageName(noinline block: () -> Unit): String = block.javaClass.packageName

@RequiresApi(Build.VERSION_CODES.S)
fun getRawFolderPath(video: Int) {
    val pkg = packageName{}
    Uri.parse("android.resource://" + pkg + "/" + video)
}*/

private fun getFileName(resolver: ContentResolver, uri: Uri): String {
    val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
    val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}
fun getRawUri(filename: String,packagename: String): Uri {
    return Uri.parse((ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator+"/" + packagename).toString() + "/raw/" + filename)
}

fun llamaApi(){
    val body = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/${MainActivity.nombreArchivo}")
    val requestFile: RequestBody =
        RequestBody.create(
           "application/octet-stream".toMediaType(),
            body
        )
    val multipartBody = MultipartBody.Part.createFormData("elmo","elmo.mp3",requestFile)
   // val repository : Repository()
   // repository.subirAudio(multipartBody)
    GlobalScope.launch {
        val result = InitializeRetrofit.todoApi.subirAudio(multipartBody);
        if (result != null)
        // Checking the results
            Log.d("AYUDA: ", result.toString())
    }
}
suspend fun SendRequest(){
val client = OkHttpClient()
val context = MainActivity.appContext
/**val uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(context.packageName)
    .appendPath("${R.raw.elmo}")
    .build()*/
val uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .path(R.raw.elmo.toString())
    .build()
   // val uri = getRawUri("elmo.mp3",context.packageName)
//val uri = Uri.parse("android.resource://viu.wearables.speechtotext/raw/elmo.mp3");
//println(video.getPath())

//val mediaType = "audio/mpeg".toMediaType()
//val path = "android.resource://" + "viu.wearables.speechtotext" + "/raw/elmo"

val body = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/${MainActivity.nombreArchivo}").asRequestBody("application/octet-stream".toMediaType())
val request = Request.Builder()
.url("http://192.168.100.156:80/")
.post(body)
.addHeader("nombrearchivo", "elmo.mp3")
.addHeader("Content-Type", "audio/mpeg")
.build()
val response = client.newCall(request).execute()
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeechToTextTheme {
        Greeting("Android")
    }
}