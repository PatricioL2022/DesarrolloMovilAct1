package viu.wearables.speechtotext.presentation.models

import kotlinx.serialization.Serializable

@Serializable
data class SpeechToText (
    val transcript: String = "", val confidence: Float = 0f, val seconds: Float = 0f
)