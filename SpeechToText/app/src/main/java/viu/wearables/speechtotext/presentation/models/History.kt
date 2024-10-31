package viu.wearables.speechtotext.presentation.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla para almacenar el historial de audios
 * procesados por el servidor
 */
@Entity(tableName = "History")
data class History (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val nombreAudio: String = "",
    val fecha: String = "",
    val hora: String = "",
    val transcripcion: String = "",
    val confianza: Float = 0f,
    val tiempoProcesamiento: Float = 0f,
    val unidadMedidaTiempoProcesamiento: String = "segundos"
)