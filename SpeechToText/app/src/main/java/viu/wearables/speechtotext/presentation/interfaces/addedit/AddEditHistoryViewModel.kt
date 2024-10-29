package viu.wearables.speechtotext.presentation.interfaces.addedit

import androidx.lifecycle.ViewModel
import viu.wearables.speechtotext.data.HistoryDAO
import viu.wearables.speechtotext.data.HistoryData
import viu.wearables.speechtotext.presentation.AddEditHistoryEvent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import viu.wearables.speechtotext.data.toEntity

class AddEditHistoryViewModel (val dao: HistoryDAO, historyId: Int = -1) : ViewModel() {
    private val _history = mutableStateOf(HistoryData())
    val history: State<HistoryData> = _history

    private fun findHistory(historyId: Int) {
        viewModelScope.launch {
            val historyEntity = dao.getHistory(historyId)
            _history.value = historyEntity?.let { HistoryData.fromEntity (it)} ?: HistoryData()
        }


    }

    init {
        findHistory(historyId)
    }

    fun onEvent(event: AddEditHistoryEvent) {
        when (event) {
            is AddEditHistoryEvent.EnteredNombreAudio -> {
                _history.value = _history.value.copy(nombreAudio = event.nombreAudio)
            }
            is AddEditHistoryEvent.EnteredFecha -> {
                _history.value = _history.value.copy(fecha = event.fecha)
            }
            is AddEditHistoryEvent.EnteredHora -> {
                _history.value = _history.value.copy(hora = event.hora)
            }
            is AddEditHistoryEvent.EnteredTranscripcion -> {
                _history.value = _history.value.copy(transcripcion = event.transcripcion)
            }
            is AddEditHistoryEvent.EnteredConfianza -> {
                _history.value = _history.value.copy(confianza = event.confianza)
            }
            is AddEditHistoryEvent.EnteredTiempoProcesamiento -> {
                _history.value = _history.value.copy(tiempoProcesamiento = event.tiempoProcesamiento)
            }
            is AddEditHistoryEvent.EnteredUnidadProcesamiento -> {
                _history.value = _history.value.copy(unidadMedidaTiempoProcesamiento = event.unidadProcesamiento)
            }

            AddEditHistoryEvent.SaveHistory -> {
                viewModelScope.launch {
                    val entity = history.value.toEntity()
                    dao.upsertHistory(entity)
                }


            }

        }

    }
}