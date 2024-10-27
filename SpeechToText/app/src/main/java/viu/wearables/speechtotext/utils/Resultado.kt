package viu.wearables.speechtotext.utils

sealed class Resultado<T> (val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resultado<T>(data)
    class Error<T>(message: String, data: T? = null) : Resultado<T>(data, message)
    class Loading<T> (data: T? = null) : Resultado<T>(data)
}