package viu.wearables.speechtotext.repository

import android.util.Log
import okhttp3.MultipartBody
import viu.wearables.speechtotext.retrofit.Api
import viu.wearables.speechtotext.retrofit.InitializeRetrofit
import viu.wearables.speechtotext.utils.Resultado
import java.io.Serializable

class Repository() {
    suspend fun subirAudio(body: MultipartBody.Part): Resultado<Serializable> {

        val response = try {
            InitializeRetrofit.todoApi.subirAudio(body)
        }catch (e:Exception){
            return Resultado.Error(e.message ?: "Error desconocido")
        }
        return Resultado.Success(response)
    }

}