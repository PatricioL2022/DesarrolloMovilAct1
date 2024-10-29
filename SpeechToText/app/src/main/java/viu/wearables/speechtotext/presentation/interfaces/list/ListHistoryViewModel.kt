package viu.wearables.speechtotext.presentation.interfaces.list
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import viu.wearables.speechtotext.data.HistoryDAO
import viu.wearables.speechtotext.data.HistoryData
import viu.wearables.speechtotext.data.toEntity
import viu.wearables.speechtotext.presentation.components.HistoryEvent
import viu.wearables.speechtotext.presentation.components.SortByTitle
import viu.wearables.speechtotext.presentation.components.SortOrder

class ListHistoryViewModel(val dao: HistoryDAO) : ViewModel() {
    private val _histories: MutableState<List<HistoryData>> = mutableStateOf(emptyList())
    var histories: State<List<HistoryData>> = _histories

    private var _sortOrder: MutableState<SortOrder> = mutableStateOf(SortByTitle)
    var sortOrder: State<SortOrder> = _sortOrder

    //Job para realizar la interacción con la BBDD con corrutinas
    var job: Job? = null

    init {
        loadHistory(sortOrder.value)
        //_histories.value= getHistories(sortOrder.value)
    }

    private  fun loadHistory(sortOrder: SortOrder) {
        job?.cancel()

        job = dao.getHistories().onEach { histories->
            delay(1000)
            _histories.value = histories.map{
                HistoryData.fromEntity(it)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HistoryEvent) {
        when(event) {
            is HistoryEvent.Delete -> {deleteHistory(event.historyData)}
            is HistoryEvent.Order -> {
                _sortOrder.value = event.order
            }
        }
    }

    private fun deleteHistory(history: HistoryData) {

        viewModelScope.launch {
            dao.deleteHistory(history.toEntity())
        }
    }
}