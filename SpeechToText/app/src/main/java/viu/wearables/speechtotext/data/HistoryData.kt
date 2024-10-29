package viu.wearables.speechtotext.data

import viu.wearables.speechtotext.presentation.models.History
import kotlin.random.Random

data class HistoryData (
    val id: Int = Random.nextInt(),
    val nombreAudio: String = "",
    val fecha: String = "",
    val hora: String = "",
    val transcripcion: String = "",
    val confianza: Float = 0f,
    val tiempoProcesamiento: Float = 0f,
    val unidadMedidaTiempoProcesamiento: String = "segundos"
)
{
    companion object {
        fun fromEntity(entity: History): HistoryData {
            return HistoryData(
                id = entity.id!!,
                nombreAudio = entity.nombreAudio,
                fecha = entity.fecha,
                hora = entity.hora,
                transcripcion = entity.transcripcion,
                confianza = entity.confianza,
                tiempoProcesamiento = entity.tiempoProcesamiento,
                unidadMedidaTiempoProcesamiento = entity.unidadMedidaTiempoProcesamiento
            )
        }
    }
}

fun HistoryData.toEntity(): History {
    val id = if (this.id == -1) null else this.id
    return History(
        id = id,
        nombreAudio = this.nombreAudio,
        fecha = this.fecha,
        hora = this.hora,
        transcripcion = this.transcripcion,
        confianza = this.confianza,
        tiempoProcesamiento = this.tiempoProcesamiento,
        unidadMedidaTiempoProcesamiento = this.unidadMedidaTiempoProcesamiento
    )
}