package viu.wearables.speechtotext.presentation

sealed interface AddEditHistoryEvent {
    data class EnteredNombreAudio(val nombreAudio: String) : AddEditHistoryEvent
    data class EnteredFecha(val fecha: String): AddEditHistoryEvent
    data class EnteredHora(val hora: String): AddEditHistoryEvent
    data class EnteredTranscripcion(val transcripcion: String): AddEditHistoryEvent
    data class EnteredConfianza(val confianza: Float): AddEditHistoryEvent
    data class EnteredTiempoProcesamiento(val tiempoProcesamiento: Float): AddEditHistoryEvent
    data class EnteredUnidadProcesamiento(val unidadProcesamiento: String): AddEditHistoryEvent
    data object SaveHistory: AddEditHistoryEvent
}