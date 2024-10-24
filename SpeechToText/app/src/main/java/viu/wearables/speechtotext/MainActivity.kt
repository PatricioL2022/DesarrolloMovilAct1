package viu.wearables.speechtotext

import android.R
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import viu.wearables.speechtotext.ui.theme.SpeechToTextTheme
import java.io.File
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechToTextTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    //switchDispatcher()
                }
            }
        }
    }
}
private fun switchDispatcher() {
    val scope = CoroutineScope(Dispatchers.Main)

    scope.launch {
        val user = withContext(Dispatchers.IO) {
            // some long task for retrieve user from REST or database
            SendRequest()
        }

    }
}
suspend fun SendRequest(){
val client = OkHttpClient()
val video = Uri.parse("android.resource://viu.wearables.speechtotext/raw/elmo");
println(video.getPath())

//val mediaType = "audio/mpeg".toMediaType()
//val path = "android.resource://" + "viu.wearables.speechtotext" + "/raw/elmo"
/**val body = File(video.getPath()).asRequestBody("application/octet-stream".toMediaType())
val request = Request.Builder()
.url("http://192.168.100.156:80/")
.post(body)
.addHeader("nombrearchivo", "elmo.mp3")
.addHeader("Content-Type", "audio/mpeg")
.build()
val response = client.newCall(request).execute()*/
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