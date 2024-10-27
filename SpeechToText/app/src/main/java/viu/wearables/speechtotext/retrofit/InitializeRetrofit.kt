package viu.wearables.speechtotext.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object InitializeRetrofit {
    private const val BASE_URL = "http://192.168.100.156:80"
    val todoApi: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}