package viu.wearables.speechtotext.retrofit

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {
    @Multipart
    @POST("/")
    suspend fun subirAudio(@Part file: MultipartBody.Part): Array<String>
}