package viu.wearables.speechtotext.retrofit

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import viu.wearables.speechtotext.presentation.models.SpeechToText

/**
 * Interfaz donde se definen las interacciones con el servidor web
 * mediante api rest especificando la dirección url y los
 * parametros de entrada y salida
 */
interface Api {
    @Multipart
    @POST("/")
    suspend fun subirAudio(@Part file: MultipartBody.Part): SpeechToText
}