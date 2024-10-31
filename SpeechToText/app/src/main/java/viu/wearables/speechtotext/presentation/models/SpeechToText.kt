package viu.wearables.speechtotext.presentation.models

import kotlinx.serialization.Serializable

/**
 * Objeto devuelto por el servidor como respuesta
 * a la petición realizada mediante api rest
 */
@Serializable
data class SpeechToText (
    val transcript: String = "", val confidence: Float = 0f, val seconds: Float = 0f
)